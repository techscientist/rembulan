/*
 * Copyright 2016 Miroslav Janíček
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sandius.rembulan.parser.ast;

import net.sandius.rembulan.LuaFormat;
import net.sandius.rembulan.util.Check;

public class StringLiteral extends Literal {

	private final String value;

	private StringLiteral(String value) {
		this.value = value;
	}

	private static int hexValueOf(char c) {
		return c >= '0' && c <= '9'
				? (int) c - (int) '0'
				: (c >= 'a' && c <= 'f'
						? 10 + (int) c - (int) 'a'
						: (c >= 'A' && c <= 'F'
								? 10 + (int) c - (int) 'A'
								: -1));
	}

	private static String stringValueOf(String s) {
		Check.notNull(s);

		StringBuilder bld = new StringBuilder();

		// skip the surrounding quotation marks
		int i = 1;
		while (i < s.length() - 1) {
			char c = s.charAt(i++);

			if (c == '\\') {
				// escaped character

				int ii = i - 1;

				c = s.charAt(i);

				if (c >= '0' && c <= '9') {
					// decimal char specification: at most three such characters

					int dec = 0;
					for (int j = 0; j < 3; j++) {
						c = s.charAt(i++);
						if (c >= '0' && c <= '9') {
							dec = dec * 10 + ((int) c - (int) '0');
						}
						else {
							i -= 1;  // put back
							break;
						}
					}

					if (dec > 255) {
						// TODO:
						throw new IllegalArgumentException("decimal escape too large at index " + ii);
					}
					else {
						bld.append((char) dec);
					}
				}
				else if (c == 'x') {
					// hexadecimal escape
					i += 1;  // skip 'x'
					int hex = 0;
					for (int j = 0; j < 2; j++) {
						c = s.charAt(i++);

						int digit = hexValueOf(c);

						if (digit == -1) {
							throw new IllegalArgumentException("hexadecimal digit expected at index " + i);
						}
						else {
							hex = (hex << 4) + digit;
						}
					}

					bld.append((char) hex);
				}
				else if (c == 'u') {
					if (s.charAt(++i) != '{') {
						throw new IllegalArgumentException("missing '{' at index " + i);
					}

					int value;

					{
						c = s.charAt(++i);
						int digit = hexValueOf(c);
						if (digit == -1) {
							throw new IllegalArgumentException("hexadecimal digit expected at index " + (i - 1));
						}
						value = digit;
					}

					do {
						c = s.charAt(++i);
						int digit = hexValueOf(c);
						if (c != '}') {
							if (digit == -1) {
								throw new IllegalArgumentException("hexadecimal digit expected at index " + (i - 1));
							}
							else {
								value = (value << 4) + digit;
							}
						}
					} while (c != '}');

					i += 1;

					bld.append((char) value);
				}
				else if (c == 'z') {
					// skip subsequent whitespace
					while (Character.isWhitespace(s.charAt(++i))) {
						// do nothing
					}
				}
				else if (c == '\n' || c == '\r') {
					if (c == '\r') {
						// this must be a \r\n
						if (s.charAt(++i) != '\n') {
							throw new IllegalArgumentException("\\n expected at index " + i);
						}
					}
					i += 1;
					bld.append('\n');
				}
				else {
					final char d;
					i += 1;
					switch (c) {
						case 'a': d = LuaFormat.CHAR_BELL; break;
						case 'b': d = '\b'; break;
						case 'f': d = '\f'; break;
						case 'n': d = '\n'; break;
						case 'r': d = '\r'; break;
						case 't': d = '\t'; break;
						case 'v': d = LuaFormat.CHAR_VERTICAL_TAB; break;
						case '\\': d = '\\'; break;
						case '\'': d = '\''; break;
						case '"': d = '"'; break;
						default:
							throw new IllegalArgumentException("invalid escape sequence at index " + ii + " (\\" + c + ")");
					}
					bld.append(d);
				}
			}
			else {
				bld.append(c);
			}
		}

		return bld.toString();
	}

	public static StringLiteral fromString(String s) {
		return new StringLiteral(stringValueOf(s));
	}

	public static StringLiteral verbatim(String s) {
		return new StringLiteral(s);
	}

	public static StringLiteral fromName(Name n) {
		return new StringLiteral(n.value());
	}

	public String value() {
		return value;
	}

	@Override
	public Literal accept(Transformer tf) {
		return tf.transform(this);
	}

}
