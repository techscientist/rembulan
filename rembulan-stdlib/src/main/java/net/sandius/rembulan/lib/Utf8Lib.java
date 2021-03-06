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
 *
 * --
 * Portions of this file are licensed under the Lua license. For Lua
 * licensing details, please visit
 *
 *     http://www.lua.org/license.html
 *
 * Copyright (C) 1994-2016 Lua.org, PUC-Rio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.sandius.rembulan.lib;

import net.sandius.rembulan.Table;
import net.sandius.rembulan.TableFactory;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * <p>This library provides basic support for UTF-8 encoding. It provides all its functions
 * inside the table {@code utf8}. This library does not provide any support for Unicode other
 * than the handling of the encoding. Any operation that needs the meaning of a character,
 * such as character classification, is outside its scope.</p>
 *
 * <p>Unless stated otherwise, all functions that expect a byte position as a parameter assume
 * that the given position is either the start of a byte sequence or one plus the length of the
 * subject string. As in the string library, negative indices count from the end
 * of the string.</p>
 */
public abstract class Utf8Lib extends Lib {

	@Override
	public String name() {
		return "utf8";
	}

	@Override
	public Table toTable(TableFactory tableFactory) {
		Table t = tableFactory.newTable();
		t.rawset("char", _char());
		t.rawset("charpattern", _charpattern());
		t.rawset("codes", _codes());
		t.rawset("codepoint", _codepoint());
		t.rawset("len", _len());
		t.rawset("offset", _offset());
		return t;
	}

	/**
	 * {@code utf8.char (···)}
	 *
	 * <p>Receives zero or more integers, converts each one to its corresponding UTF-8
	 * byte sequence and returns a string with the concatenation of all these sequences.</p>
	 *
	 * @return the {@code utf8.char} function
	 */
	public abstract LuaFunction _char();

	/**
	 * {@code utf8.charpattern}
	 *
	 * <p>The pattern (a string, not a function) "{@code [\0-\x7F\xC2-\xF4][\x80-\xBF]*}"
	 * (see §6.4.1), which matches exactly one UTF-8 byte sequence, assuming that the subject is
	 * a valid UTF-8 string.</p>
	 *
	 * @return the {@code utf8.charpattern} string
	 */
	public abstract String _charpattern();

	/**
	 * {@code utf8.codes (s)}
	 *
	 * <p>Returns values so that the construction</p>
	 * <pre>
	 * {@code
	 * for p, c in utf8.codes(s) do body end
	 * }
	 * </pre>
	 * <p>will iterate over all characters in string {@code s}, with {@code p} being the position
	 * (in bytes) and {@code c} the code point of each character. It raises an error if it meets
	 * any invalid byte sequence.</p>
	 *
	 * @return the {@code utf8.codes} function
	 */
	public abstract LuaFunction _codes();

	/**
	 * {@code utf8.codepoint (s [, i [, j]])}
	 *
	 * <p>Returns the codepoints (as integers) from all characters in {@code s} that start between
	 * byte position {@code i} and {@code j} (both included). The default for {@code i} is 1
	 * and for {@code j} is {@code i}. It raises an error if it meets any invalid byte
	 * sequence.</p>
	 *
	 * @return the {@code utf8.codepoint} function
	 */
	public abstract LuaFunction _codepoint();

	/**
	 * {@code utf8.len (s [, i [, j]])}
	 *
	 * <p>Returns the number of UTF-8 characters in string {@code s} that start between positions
	 * {@code i} and {@code j} (both inclusive). The default for {@code i} is 1
	 * and for {@code j} is -1. If it finds any invalid byte sequence, returns a <b>false</b>
	 * value plus the position of the first invalid byte.</p>
	 *
	 * @return the {@code utf8.len} function
	 */
	public abstract LuaFunction _len();

	/**
	 * {@code utf8.offset (s, n [, i])}
	 *
	 * <p>Returns the position (in bytes) where the encoding of the {@code n}-th character
	 * of {@code s} (counting from position {@code i}) starts. A negative {@code n} gets
	 * characters before position {@code i}. The default for {@code i} is 1 when {@code n}
	 * is non-negative and {@code #s + 1} otherwise, so that {@code utf8.offset(s, -n)}
	 * gets the offset of the {@code n}-th character from the end of the string. If the specified
	 * character is neither in the subject nor right after its end, the function
	 * returns <b>nil</b>.</p>
	 *
	 * <p>As a special case, when {@code n} is 0 the function returns the start of the encoding
	 * of the character that contains the {@code i}-th byte of {@code s}.</p>
	 *
	 * <p>This function assumes that {@code s} is a valid UTF-8 string.</p>
	 *
	 * @return the {@code utf8.offset} function
	 */
	public abstract LuaFunction _offset();

}
