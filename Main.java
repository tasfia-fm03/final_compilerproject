import java.util.List;

public class Main {
    public static void main(String[] args) {
        String source = "সংখ্যা x : ১০; দেখো x;";
        List<Token> tokens = new l_lexer(source).tokenize();
        System.out.println("Lexer completed: " + tokens.size() + " tokens.");

        List<structure_ast> statements = List.of(
            new VarDeclStmt("x", new LiteralExpr(10), 1),
            new Printstructure_ast(new VariableExpr("x", 1), 1)
        );
        new Interpretor().interpret(statements);
    }
}
