# Bangla lexer and interpreter example

This version has no parser or semantic analyzer. `Main.java` tokenizes a short
Bangla source example to demonstrate the lexer, then constructs equivalent AST
objects directly for the interpreter. Editing `source` alone will not change what
the interpreter runs; edit the AST statements too.

JDK 17 or later, in PowerShell from this folder:

```powershell
javac (Get-ChildItem -File -Filter '*.java' | ForEach-Object Name)
java Main
```

The interpreter should print `Output: 10`.
