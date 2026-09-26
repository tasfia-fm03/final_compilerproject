public abstract class Expr {
    public abstract <R> R accept(ExprVisitor<R> visitor);
}

class BinaryExpr extends Expr {
    final Expr left;
    final banglalanguage operator;
    final Expr right;

    BinaryExpr(
            Expr left,
            banglalanguage operator,
            Expr right) {

        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitBinary(this);
    }
}

class LiteralExpr extends Expr {
    final int value;

    LiteralExpr(int value) {
        this.value = value;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitLiteral(this);
    }
}

class VariableExpr extends Expr {
    final String name;
    final int line;

    VariableExpr(String name, int line) {
        this.name = name;
        this.line = line;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitVariable(this);
    }
}