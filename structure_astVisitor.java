public interface structure_astVisitor<R> {

    R visitVarDecl(VarDeclStmt stmt);

    R visitPrint(Printstructure_ast stmt);

    R visitExpressionStmt(Expressionstructure_ast stmt);

    R visitBlock(Blockstructure_ast stmt);

    R visitIf(Ifstructure_ast stmt);

    R visitWhile(Whilestructure_ast stmt);
}