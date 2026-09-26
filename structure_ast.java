import java.util.List;

public abstract class structure_ast {
    public abstract <R> R accept(structure_astVisitor<R> visitor);
}

class VarDeclStmt extends structure_ast {
    final String name;
    final Expr initializer;
    final int line;

    VarDeclStmt(String name, Expr initializer, int line) {
        this.name = name;
        this.initializer = initializer;
        this.line = line;
    }

    @Override
    public <R> R accept(structure_astVisitor<R> visitor) {
        return visitor.visitVarDecl(this);
    }
}

class Printstructure_ast extends structure_ast {
    final Expr expression;
    final int line;

    Printstructure_ast(Expr expression, int line) {
        this.expression = expression;
        this.line = line;
    }

    @Override
    public <R> R accept(structure_astVisitor<R> visitor) {
        return visitor.visitPrint(this);
    }
}

class Expressionstructure_ast extends structure_ast {
    final Expr expression;

    Expressionstructure_ast(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(structure_astVisitor<R> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}

class Blockstructure_ast extends structure_ast {
    final List<structure_ast> statements;

    Blockstructure_ast(List<structure_ast> statements) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(structure_astVisitor<R> visitor) {
        return visitor.visitBlock(this);
    }
}

class Ifstructure_ast extends structure_ast {
    final Expr condition;
    final structure_ast thenBranch;
    final structure_ast elseBranch;

    Ifstructure_ast(
            Expr condition,
            structure_ast thenBranch,
            structure_ast elseBranch) {

        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <R> R accept(structure_astVisitor<R> visitor) {
        return visitor.visitIf(this);
    }
}

class Whilestructure_ast extends structure_ast {
    final Expr condition;
    final structure_ast body;

    Whilestructure_ast(Expr condition, structure_ast body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <R> R accept(structure_astVisitor<R> visitor) {
        return visitor.visitWhile(this);
    }
}