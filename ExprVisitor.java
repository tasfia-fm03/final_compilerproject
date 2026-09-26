public interface ExprVisitor<R> {

    R visitBinary(BinaryExpr expr);

    R visitLiteral(LiteralExpr expr);

    R visitVariable(VariableExpr expr);
}