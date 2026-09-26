import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class l_lexer {

    private final String code;
    private int position;
    private int line;
    private final List<Token> tokenss;

    public l_lexer(String code) {
        this.code = Normalizer.normalize(code, Normalizer.Form.NFC);
        this.position = 0;
        this.line = 1;
        this.tokenss = new ArrayList<>();
    }

    private Character getChar() {
        if (position >= code.length()) {
            return null;
        }

        return code.charAt(position);
    }

    private Character peekNext() {
        if (position + 1 >= code.length()) {
            return null;
        }

        return code.charAt(position + 1);
    }

    private void advance() {
        if (position < code.length()) {

            if (code.charAt(position) == '\n') {
                line++;
            }

            position++;
        }
    }

    private boolean isBanglaCharacter(char c) {
        return c >= '\u0980' && c <= '\u09FF';
    }

    private boolean isBanglaLetter(char c) {
        return isBanglaCharacter(c) && Character.isLetter(c);
    }

    private boolean isBanglaDigit(char c) {
        return c >= '০' && c <= '৯';
    }

    private boolean isBanglaHasanta(char c) {
        return c == '\u09CD';
    }

    private boolean isCombiningMark(char c) {
        int type = Character.getType(c);

        return type == Character.NON_SPACING_MARK
                || type == Character.COMBINING_SPACING_MARK
                || type == Character.ENCLOSING_MARK;
    }

    private boolean isJoiner(char c) {
        return c == '\u200C' || c == '\u200D';
    }

    private boolean isWordCharacter(char c) {

        return Character.isLetterOrDigit(c)
                || c == '_'
                || isBanglaLetter(c)
                || isBanglaDigit(c)
                || isBanglaHasanta(c)
                || isCombiningMark(c)
                || isJoiner(c);
    }

    private boolean isWordStart(char c) {

        return Character.isLetter(c)
                || c == '_'
                || isBanglaLetter(c);
    }

    public List<Token> tokenize() {

        while (true) {

            Character c = getChar();

            if (c == null) {
                break;
            }

            if (Character.isWhitespace(c)) {
                advance();
                continue;
            }

            if (Character.isDigit(c) || isBanglaDigit(c)) {

                StringBuilder num = new StringBuilder();

                while (c != null
                        && (Character.isDigit(c)
                        || isBanglaDigit(c))) {

                    num.append(c);

                    advance();

                    c = getChar();
                }

                tokenss.add(
                        new Token(
                                banglalanguage.পূর্ণসংখ্যা,
                                num.toString(),
                                line
                        )
                );

                continue;
            }

            if (isWordStart(c)) {

                StringBuilder word = new StringBuilder();

                while (c != null && isWordCharacter(c)) {

                    word.append(c);

                    advance();

                    c = getChar();
                }

                String text = word.toString();

                switch (text) {

                    case "সংখ্যা":
                        tokenss.add(
                                new Token(
                                        banglalanguage.সংখ্যা,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "দেখো":
                        tokenss.add(
                                new Token(
                                        banglalanguage.দেখো,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "যদি":
                        tokenss.add(
                                new Token(
                                        banglalanguage.যদি,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "নাহলে":
                        tokenss.add(
                                new Token(
                                        banglalanguage.নাহলে,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "নাহলে_যদি":
                        tokenss.add(
                                new Token(
                                        banglalanguage.নাহলে_যদি,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "যতক্ষণ":
                        tokenss.add(
                                new Token(
                                        banglalanguage.যতক্ষণ,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "ধরি":
                        tokenss.add(
                                new Token(
                                        banglalanguage.ধরি,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "এবং":
                        tokenss.add(
                                new Token(
                                        banglalanguage.এবং,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "বা":
                        tokenss.add(
                                new Token(
                                        banglalanguage.বা,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "সমান":
                        tokenss.add(
                                new Token(
                                        banglalanguage.সমান,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "সমান_নয়":
                        tokenss.add(
                                new Token(
                                        banglalanguage.সমান_নয়,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "বড়":
                        tokenss.add(
                                new Token(
                                        banglalanguage.বড়,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "বড়_সমান":
                        tokenss.add(
                                new Token(
                                        banglalanguage.বড়_সমান,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "ছোট":
                        tokenss.add(
                                new Token(
                                        banglalanguage.ছোট,
                                        text,
                                        line
                                )
                        );
                        break;

                    case "ছোট_সমান":
                        tokenss.add(
                                new Token(
                                        banglalanguage.ছোট_সমান,
                                        text,
                                        line
                                )
                        );
                        break;

                    default:
                        tokenss.add(
                                new Token(
                                        banglalanguage.পরিচায়ক,
                                        text,
                                        line
                                )
                        );
                        break;
                }

                continue;
            }

            if (c == '='
                    && peekNext() != null
                    && peekNext() == '=') {

                advance();
                advance();

                tokenss.add(
                        new Token(
                                banglalanguage.সমান,
                                "==",
                                line
                        )
                );

                continue;
            }

            if (c == '!'
                    && peekNext() != null
                    && peekNext() == '=') {

                advance();
                advance();

                tokenss.add(
                        new Token(
                                banglalanguage.সমান_নয়,
                                "!=",
                                line
                        )
                );

                continue;
            }

            if (c == '>'
                    && peekNext() != null
                    && peekNext() == '=') {

                advance();
                advance();

                tokenss.add(
                        new Token(
                                banglalanguage.বড়_সমান,
                                ">=",
                                line
                        )
                );

                continue;
            }

            if (c == '<'
                    && peekNext() != null
                    && peekNext() == '=') {

                advance();
                advance();

                tokenss.add(
                        new Token(
                                banglalanguage.ছোট_সমান,
                                "<=",
                                line
                        )
                );

                continue;
            }

            if (c == '>') {

                advance();

                tokenss.add(
                        new Token(
                                banglalanguage.বড়,
                                ">",
                                line
                        )
                );

                continue;
            }

            if (c == '<') {

                advance();

                tokenss.add(
                        new Token(
                                banglalanguage.ছোট,
                                "<",
                                line
                        )
                );

                continue;
            }

            switch (c) {

                case ':':
                    tokenss.add(
                            new Token(
                                    banglalanguage.নির্ধারণ_করা,
                                    ":",
                                    line
                            )
                    );
                    break;

                case '+':
                    tokenss.add(
                            new Token(
                                    banglalanguage.যোগ,
                                    "+",
                                    line
                            )
                    );
                    break;

                case '-':
                    tokenss.add(
                            new Token(
                                    banglalanguage.বিয়োগ,
                                    "-",
                                    line
                            )
                    );
                    break;

                case '*':
                    tokenss.add(
                            new Token(
                                    banglalanguage.গুণ,
                                    "*",
                                    line
                            )
                    );
                    break;

                case '/':
                    tokenss.add(
                            new Token(
                                    banglalanguage.ভাগ,
                                    "/",
                                    line
                            )
                    );
                    break;

                case '%':
                    tokenss.add(
                            new Token(
                                    banglalanguage.শতাংশ,
                                    "%",
                                    line
                            )
                    );
                    break;

                case ';':
                    tokenss.add(
                            new Token(
                                    banglalanguage.সেমিকোলন,
                                    ";",
                                    line
                            )
                    );
                    break;

                case '(':
                    tokenss.add(
                            new Token(
                                    banglalanguage.বাম_বন্ধনী,
                                    "(",
                                    line
                            )
                    );
                    break;

                case ')':
                    tokenss.add(
                            new Token(
                                    banglalanguage.ডান_বন্ধনী,
                                    ")",
                                    line
                            )
                    );
                    break;

                case '{':
                    tokenss.add(
                            new Token(
                                    banglalanguage.বাম_কার্লি_ব্র্যাকেট,
                                    "{",
                                    line
                            )
                    );
                    break;

                case '}':
                    tokenss.add(
                            new Token(
                                    banglalanguage.ডান_কার্লি_ব্র্যাকেট,
                                    "}",
                                    line
                            )
                    );
                    break;

                case '=':
                    tokenss.add(
                            new Token(
                                    banglalanguage.সমান,
                                    "=",
                                    line
                            )
                    );
                    break;

                case ',':
                    tokenss.add(
                            new Token(
                                    banglalanguage.কমা,
                                    ",",
                                    line
                            )
                    );
                    break;

                default:

                    System.err.println(
                            "লেক্সার ভুল: অবৈধ অক্ষর '"
                            + c
                            + "' লাইন "
                            + line
                    );

                    tokenss.add(
                            new Token(
                                    banglalanguage.অজানা,
                                    String.valueOf(c),
                                    line
                            )
                    );

                    break;
            }

            advance();
        }

        return tokenss;
    }
}