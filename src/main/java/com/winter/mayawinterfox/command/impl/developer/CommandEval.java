package com.winter.mayawinterfox.command.impl.developer;

import com.winter.mayawinterfox.Main;
import com.winter.mayawinterfox.checks.PermissionChecks;
import com.winter.mayawinterfox.command.Command;
import com.winter.mayawinterfox.data.Node;
import com.winter.mayawinterfox.util.MessageUtil;
import sx.blah.discord.handle.obj.Permissions;

import javax.script.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandEval extends Node<Command> {

	private static final Pattern CODE = Pattern.compile(".*?eval\n```(\\w+)\n((.|\n)*\n)```");
	private static final ScriptEngineManager MANAGER = new ScriptEngineManager();

	public CommandEval() {
		super(new Command(
				"eval",
				"dev-only",
				PermissionChecks.isGlobal(),
				ev -> {
					Matcher match = CODE.matcher(ev.getMessage().getContent());
					if (match.matches()) {
						String lang = match.group(1);
						String code = match.group(2);
						ScriptEngine e = MANAGER.getEngineByName(lang);
						e.getContext().setAttribute("client", Main.getClient(), ScriptContext.ENGINE_SCOPE);
						try {
							MessageUtil.sendRawMessage(ev.getChannel(), "```\n" + e.eval(code) + "\n```");
						} catch (ScriptException ex) {
							MessageUtil.sendRawMessage(ev.getChannel(), "Error in eval: ```\n" + ex.toString() + "\n```");
						}
					} else {
						MessageUtil.sendRawMessage(ev.getChannel(), "Eval must be in the format of:\n\n[prefix]eval\n\\`\\`\\`language\ncode\n\\`\\`\\`");
						StringBuilder scriptInfo = new StringBuilder();
						scriptInfo.append("JSR Info: ```\n");
						List<ScriptEngineFactory> factories = MANAGER.getEngineFactories();
						for (ScriptEngineFactory factory : factories) {
							scriptInfo.append("ScriptEngineFactory Info\n");
							String engName = factory.getEngineName();
							String engVersion = factory.getEngineVersion();
							String langName = factory.getLanguageName();
							String langVersion = factory.getLanguageVersion();
							scriptInfo.append(String.format("\tScript Engine: %s (%s)%n", engName, engVersion));
							List<String> engNames = factory.getNames();
							for (String name : engNames) {
								scriptInfo.append(String.format("\tEngine Alias: %s%n", name));
							}
							scriptInfo.append(String.format("\tLanguage: %s (%s)%n", langName, langVersion));
						}
						scriptInfo.append("```");
						MessageUtil.sendRawMessage(ev.getChannel(), scriptInfo.toString());
					}
					return false;
				}), Collections.emptyList());
	}
}
