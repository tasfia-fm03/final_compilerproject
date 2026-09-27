import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class semanticanalyzer implements structure_astVisitor<Void>, ExprVisitor<Void> {

    private final Map<String, Integer> declared = new HashMap<>();
    private final Set<String> used = new HashSet<>();
    private int errors = 0;

    public void analyze(List<structure_ast> statements) {
        for (structure_ast stmt : statements) {
            stmt.accept(this);
        }

        for (String name : declared.keySet()) {
            if (!used.contains(name)) {
                System.err.println("Warning: Variable '" + name + "' declared but never used.");
            }
        }

        if (errors > 0) {
            throw new RuntimeException("Semantic analysis failed with " + errors + " error(s).");
        }
    }

    @Override
    public Void visitVarDecl(VarDeclStmt stmt) {
        if (declared.containsKey(stmt.name)) {
            System.err.println("Semantic Error [line " + stmt.line + "]: Variable '" + stmt.name + "' already declared.");
            errors++;
        } else {
            declared.put(stmt.name, stmt.line);
        }
        stmt.initializer.accept(this);
        return null;
    }

    @Override
    public Void visitPrint(Printstructure_ast stmt) {
        stmt.expression.accept(this);
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
        stmt.condition.accept(this);
        stmt.thenBranch.accept(this);
        if (stmt.elseBranch != null) {
            stmt.elseBranch.accept(this);
        }
        return null;
    }

    @Override
    public Void visitWhile(Whilestructure_ast stmt) {
        stmt.condition.accept(this);
        stmt.body.accept(this);
        return null;
    }

    @Override
    public Void visitBinary(BinaryExpr expr) {
        expr.left.accept(this);
        expr.right.accept(this);
        return null;
    }

    @Override
    public Void visitLiteral(LiteralExpr expr) {
        return null;
    }

    @Override
    public Void visitVariable(VariableExpr expr) {
        if (!declared.containsKey(expr.name)) {
            System.err.println("Semantic Error [line " + expr.line + "]: Variable '" + expr.name + "' is not defined.");
            errors++;
        } else {
            used.add(expr.name);
        }
        return null;
    }
}