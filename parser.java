import java.util.ArrayList;
import java.util.List;

public class parser {
    private final List<Token> tokens;
    private int current = 0;

    public parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<structure_ast> parse() {
        List<structure_ast> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    private structure_ast declaration() {
        if (match(banglalanguage.সংখ্যা)) return varDeclaration();
        return statement();
    }

    private structure_ast statement() {
        if (match(banglalanguage.দেখো)) return printStatement();
        if (match(banglalanguage.যদি)) return ifStatement();
        if (match(banglalanguage.যতক্ষণ)) return whileStatement();
        if (match(banglalanguage.বাম_কার্লি_ব্র্যাকেট)) return block();
        return expressionStatement();
    }

    private structure_ast varDeclaration() {
        Token name = consume(banglalanguage.পরিচায়ক, "Expected variable name after 'সংখ্যা'");
        consume(banglalanguage.নির্ধারণ_করা, "Expected ':' after variable name");
        Expr initializer = expression();
        consume(banglalanguage.সেমিকোলন, "Expected ';' after variable declaration");
        return new VarDeclStmt(name.value, initializer, name.line);
    }

    private structure_ast printStatement() {
        Expr value = expression();
        consume(banglalanguage.সেমিকোলন, "Expected ';' after print statement");
        return new Printstructure_ast(value, previous().line);
    }

    private structure_ast ifStatement() {
        consume(banglalanguage.বাম_বন্ধনী, "Expected '(' after 'যদি'");
        Expr condition = expression();
        consume(banglalanguage.ডান_বন্ধনী, "Expected ')' after condition");

        structure_ast thenBranch = statement();
        structure_ast elseBranch = null;

        if (match(banglalanguage.নাহলে)) {
            elseBranch = statement();
        }

        return new Ifstructure_ast(condition, thenBranch, elseBranch);
    }

    private structure_ast whileStatement() {
        consume(banglalanguage.বাম_বন্ধনী, "Expected '(' after 'যতক্ষণ'");
        Expr condition = expression();
        consume(banglalanguage.ডান_বন্ধনী, "Expected ')' after condition");
        structure_ast body = statement();
        return new Whilestructure_ast(condition, body);
    }

    private structure_ast block() {
        List<structure_ast> statements = new ArrayList<>();
        while (!check(banglalanguage.ডান_কার্লি_ব্র্যাকেট) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(banglalanguage.ডান_কার্লি_ব্র্যাকেট, "Expected '}' after block");
        return new Blockstructure_ast(statements);
    }

    private structure_ast expressionStatement() {
        Expr expr = expression();
        consume(banglalanguage.সেমিকোলন, "Expected ';' after expression");
        return new Expressionstructure_ast(expr);
    }

    private Expr expression() {
        return assignment();
    }

    private Expr assignment() {
        Expr expr = or();

        if (match(banglalanguage.সমান)) {
            Token equals = previous();
            Expr value = assignment();

            if (expr instanceof VariableExpr) {
                return new BinaryExpr(expr, banglalanguage.সমান, value);
            }
            throw error(equals, "Invalid assignment target");
        }
        return expr;
    }

    private Expr or() {
        Expr expr = and();
        while (match(banglalanguage.বা)) {
            banglalanguage op = previous().type;
            Expr right = and();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = equality();
        while (match(banglalanguage.এবং)) {
            banglalanguage op = previous().type;
            Expr right = equality();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(banglalanguage.সমান, banglalanguage.সমান_নয়)) {
            banglalanguage op = previous().type;
            Expr right = comparison();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(banglalanguage.বড়, banglalanguage.বড়_সমান,
                     banglalanguage.ছোট, banglalanguage.ছোট_সমান)) {
            banglalanguage op = previous().type;
            Expr right = term();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(banglalanguage.যোগ, banglalanguage.বিয়োগ)) {
            banglalanguage op = previous().type;
            Expr right = factor();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(banglalanguage.গুণ, banglalanguage.ভাগ, banglalanguage.শতাংশ)) {
            banglalanguage op = previous().type;
            Expr right = unary();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(banglalanguage.বিয়োগ)) {
            banglalanguage op = previous().type;
            Expr right = unary();
            return new BinaryExpr(new LiteralExpr(0), op, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(banglalanguage.পূর্ণসংখ্যা)) {
            return new LiteralExpr(parseNumber(previous().value));
        }
        if (match(banglalanguage.পরিচায়ক)) {
            return new VariableExpr(previous().value, previous().line);
        }
        if (match(banglalanguage.বাম_বন্ধনী)) {
            Expr expr = expression();
            consume(banglalanguage.ডান_বন্ধনী, "Expected ')' after expression");
            return expr;
        }
        throw error(peek(), "Expected expression");
    }

    private boolean match(banglalanguage... types) {
        for (banglalanguage type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(banglalanguage type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private boolean check(banglalanguage type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private RuntimeException error(Token token, String message) {
        String where = (token == null) ? "end of file" : "line " + token.line;
        return new RuntimeException("Parse Error [" + where + "]: " + message);
    }

    private int parseNumber(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= '০' && c <= '৯') {
                sb.append((char) ('0' + (c - '০')));
            } else {
                sb.append(c);
            }
        }
        return Integer.parseInt(sb.toString());
    }
}