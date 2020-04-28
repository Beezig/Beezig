# How to contribute
Thank you for the time you're putting into this project.

**If you found a bug, make sure to report it in the Issues section.**

If, instead, you wanted to propose a feature, you can either fork the project and open a Pull Request or you can propose it in the
#suggestions channel in our [Discord server](http://discord.gg/se7zJsU).

In that server you can also ask questions regarding the project.

Thanks again,  
the Beezig team.

## Information about the rewrite (code style)

#### Class names
PascalCase, when the name is part of a category, put the category before the name.  
Example: `ServerHive` instead of `HiveServer`.  
**EXCEPTION**: If the name is uppercase, use the normal format.  
Example: `TIMVListener` instead of `ListenerTIMV`.

#### Java 8 features
Always, good readability

#### Minecraft functions
Never, breaks version compatibility

#### 5zig API
Only use `ModAPI` (`Beezig.api()`), as BeezigLaby only supports that subset of it.