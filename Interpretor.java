import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpretor implements structure_astVisitor<Void>, ExprVisitor<Integer> {

    private final Map<String, Integer> memory = new HashMap<>();

    public void interpret(List<structure_ast> statements) {
        for (structure_ast statement : statements) {
            statement.accept(this);
        }
    }

    @Override
    public Void visitVarDecl(VarDeclStmt stmt) {
        int value = stmt.initializer.accept(this);
        memory.put(stmt.name, value);
        return null;
    }

    @Override
    public Void visitPrint(Printstructure_ast stmt) {
        int result = stmt.expression.accept(this);
        System.out.println("আউটপুট: " + result);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expressionstructure_ast stmt) {
        stmt.expression.accept(this);
        return null;
    }

    @Override
    public Void visitBlock(Blockstructure_ast stmt) {
        for (structure_ast s : stmt.statements) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void visitIf(Ifstructure_ast stmt) {
        int condition = stmt.condition.accept(this);
        if (condition != 0) {
            stmt.thenBranch.accept(this);
        } else if (stmt.elseBranch != null) {
            stmt.elseBranch.accept(this);
        }
        return null;
    }

    @Override
    public Void visitWhile(Whilestructure_ast stmt) {
        while (stmt.condition.accept(this) != 0) {
            stmt.body.accept(this);
        }
        return null;
    }

    @Override
    public Integer visitBinary(BinaryExpr expr) {
        int left = expr.left.accept(this);
        int right = expr.right.accept(this);

        return switch (expr.operator) {
            case যোগ -> left + right;
            case বিয়োগ -> left - right;
            case গুণ -> left * right;
            case ভাগ -> {
                if (right == 0) throw new RuntimeException("Runtime Error: Division by zero");
                yield left / right;
            }
            case শতাংশ -> left % right;
            case সমান -> left == right ? 1 : 0;
            case সমান_নয় -> left != right ? 1 : 0;
            case বড় -> left > right ? 1 : 0;
            case বড়_সমান -> left >= right ? 1 : 0;
            case ছোট -> left < right ? 1 : 0;
            case ছোট_সমান -> left <= right ? 1 : 0;
            case এবং -> (left != 0 && right != 0) ? 1 : 0;
            case বা -> (left != 0 || right != 0) ? 1 : 0;
            default -> throw new RuntimeException("Unknown operator: " + expr.operator);
        };
    }

    @Override
    public Integer visitLiteral(LiteralExpr expr) {
        return expr.value;
    }

    @Override
    public Integer visitVariable(VariableExpr expr) {
        if (!memory.containsKey(expr.name)) {
            throw new RuntimeException("Runtime Error: Variable '" + expr.name + "' is not defined.");
        }
        return memory.get(expr.name);
    }
}