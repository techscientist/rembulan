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
 * This library provides basic mathematical functions. It provides all its functions and constants
 * inside the table {@code math}. Functions with the annotation "integer/float" give integer
 * results for integer arguments and float results for float (or mixed) arguments. Rounding
 * functions ({@link #_ceil() {@code math.ceil}}, {@link #_floor() {@code math.floor}},
 * and {@link #_modf() {@code math.modf}}) return an integer when the result fits
 * in the range of an integer, or a float otherwise.
 */
public abstract class MathLib extends Lib {

	@Override
	public String name() {
		return "math";
	}

	@Override
	public Table toTable(TableFactory tableFactory) {
		Table t = tableFactory.newTable();
		t.rawset("abs", _abs());
		t.rawset("acos", _acos());
		t.rawset("asin", _asin());
		t.rawset("atan", _atan());
		t.rawset("ceil", _ceil());
		t.rawset("cos", _cos());
		t.rawset("deg", _deg());
		t.rawset("exp", _exp());
		t.rawset("floor", _floor());
		t.rawset("fmod", _fmod());
		t.rawset("huge", _huge());
		t.rawset("log", _log());
		t.rawset("max", _max());
		t.rawset("maxinteger", _maxinteger());
		t.rawset("min", _min());
		t.rawset("mininteger", _mininteger());
		t.rawset("modf", _modf());
		t.rawset("pi", _pi());
		t.rawset("rad", _rad());
		t.rawset("random", _random());
		t.rawset("randomseed", _randomseed());
		t.rawset("sin", _sin());
		t.rawset("sqrt", _sqrt());
		t.rawset("tan", _tan());
		t.rawset("tointeger", _tointeger());
		t.rawset("type", _type());
		t.rawset("ult", _ult());
		return t;
	}

	/**
	 * {@code math.abs (x)}
	 *
	 * <p>Returns the absolute value of {@code x}. (integer/float)</p>
	 *
	 * @return the {@code math.abs} function
	 */
	public abstract LuaFunction _abs();

	/**
	 * {@code math.acos (x)}
	 *
	 * <p>Returns the arc cosine of {@code x} (in radians).</p>
	 *
	 * @return the {@code math.acos} function
	 */
	public abstract LuaFunction _acos();

	/**
	 * {@code math.asin (x)}
	 *
	 * <p>Returns the arc sine of {@code x} (in radians).</p>
	 *
	 * @return the {@code math.asin} function
	 */
	public abstract LuaFunction _asin();

	/**
	 * {@code math.atan (x)}
	 *
	 * <p>Returns the arc tangent of {@code y/x} (in radians), but uses the signs of both
	 * parameters to find the quadrant of the result. (It also handles correctly the case
	 * of {@code x} being zero.)</p>
	 *
	 * <p>The default value for {@code x} is 1, so that the call {@code math.atan(y)} returns
	 * the arc tangent of {@code y}.</p>
	 *
	 * @return the {@code math.atan} function
	 */
	public abstract LuaFunction _atan();

	/**
	 * {@code math.ceil (x)}
	 *
	 * <p>Returns the smallest integer larger than or equal to {@code x}.</p>
	 *
	 * @return the {@code math.ceil} function
	 */
	public abstract LuaFunction _ceil();

	/**
	 * {@code math.cos (x)}
	 *
	 * <p>Returns the cosine of {@code x} (assumed to be in radians).</p>
	 *
	 * @return the {@code math.cos} function
	 */
	public abstract LuaFunction _cos();

	/**
	 * {@code math.deg (x)}
	 *
	 * <p>Returns the angle {@code x} (given in radians) in degrees.</p>
	 *
	 * @return the {@code math.deg} function
	 */
	public abstract LuaFunction _deg();

	/**
	 * {@code math.exp (x)}
	 *
	 * <p>Returns the value <i>e</i><sup>{@code x}</sup> (where <i>e</i> is the base
	 * of natural logarithms).</p>
	 *
	 * @return the {@code math.exp} function
	 */
	public abstract LuaFunction _exp();

	/**
	 * {@code math.floor (x)}
	 *
	 * <p>Returns the largest integral value smaller than or equal to {@code x}.</p>
	 *
	 * @return the {@code math.floor} function
	 */
	public abstract LuaFunction _floor();

	/**
	 * {@code math.fmod (x, y)}
	 *
	 * <p>Returns the remainder of the division of {@code x} by {@code y} that rounds
	 * the quotient towards zero. (integer/float)</p>
	 *
	 * @return the {@code math.fmod} function
	 */
	public abstract LuaFunction _fmod();

	/**
	 * {@code math.huge}
	 *
	 * <p>The value {@code HUGE_VAL}, a value larger than or equal to any other numerical
	 * value.</p>
	 *
	 * @return the {@code math.huge} number
	 */
	public abstract Double _huge();

	/**
	 * {@code math.log (x [, base])}
	 *
	 * <p>Returns the logarithm of {@code x} in the given base. The default for {@code base}
	 * is <i>e</i> (so that the function returns the natural logarithm of {@code x}).</p>
	 *
	 * @return the {@code math.log} function
	 */
	public abstract LuaFunction _log();

	/**
	 * {@code math.max (x, ···)}
	 *
	 * <p>Returns the argument with the maximum value, according to the Lua operator &lt;.
	 * (integer/float)</p>
	 *
	 * @return the {@code math.max} function
	 */
	public abstract LuaFunction _max();

	/**
	 * An integer with the maximum value for an integer.
	 *
	 * @return the {@code math.maxinteger} number
	 */
	public abstract Long _maxinteger();

	/**
	 * {@code math.min (x, ···)}
	 *
	 * <p>Returns the argument with the minimum value, according to the Lua operator &lt;.
	 * (integer/float)</p>
	 *
	 * @return the {@code math.min} function
	 */
	public abstract LuaFunction _min();

	/**
	 * An integer with the minimum value for an integer.
	 *
	 * @return the {@code math.mininteger} number
	 */
	public abstract Long _mininteger();

	/**
	 * {@code math.modf (x)}
	 *
	 * <p>Returns the integral part of {@code x} and the fractional part of {@code x}.
	 * Its second result is always a float.</p>
	 *
	 * @return the {@code math.modf} function
	 */
	public abstract LuaFunction _modf();

	/**
	 * {@code math.pi}
	 *
	 * <p>The value of &pi;.</p>
	 *
	 * @return the {@code math.pi} number
	 */
	public abstract Double _pi();

	/**
	 * {@code math.rad (x)}
	 *
	 * <p>Returns the angle {@code x} (given in degrees) in radians.</p>
	 *
	 * @return the {@code math.rad} function
	 */
	public abstract LuaFunction _rad();

	/**
	 * {@code math.random ([m [, n]])}
	 *
	 * <p>When called without arguments, returns a uniform pseudo-random real number
	 * in the range <i>[0,1)</i>. When called with an integer number {@code m},
	 * {@code math.random} returns a uniform pseudo-random integer in the range
	 * <i>[1, m]</i>. When called with two integer numbers {@code m} and {@code n},
	 * {@code math.random} returns a uniform pseudo-random integer in the range
	 * <i>[m, n]</i>.</p>
	 *
	 * <p>This function is an interface to the simple pseudo-random generator function
	 * {@code rand} provided by Standard C. (No guarantees can be given for its statistical
	 * properties.)</p>
	 *
	 * @return the {@code math.random} function
	 */
	public abstract LuaFunction _random();

	/**
	 * {@code math.randomseed (x)}
	 *
	 * <p>Sets {@code x} as the "seed" for the pseudo-random generator: equal seeds produce
	 * equal sequences of numbers.</p>
	 *
	 * @return the {@code math.randomseed} function
	 */
	public abstract LuaFunction _randomseed();

	/**
	 * {@code math.sin (x)}
	 *
	 * <p>Returns the sine of {@code x} (assumed to be in radians).</p>
	 *
	 * @return the {@code math.sin} function
	 */
	public abstract LuaFunction _sin();

	/**
	 * {@code math.sqrt (x)}
	 *
	 * <p>Returns the square root of {@code x}. (You can also use the expression {@code x^0.5}
	 * to compute this value.)</p>
	 *
	 * @return the {@code math.sqrt} function
	 */
	public abstract LuaFunction _sqrt();

	/**
	 * {@code math.tan (x)}
	 *
	 * <p>Returns the tangent of {@code x} (assumed to be in radians).</p>
	 *
	 * @return the {@code math.tan} function
	 */
	public abstract LuaFunction _tan();

	/**
	 * {@code math.tointeger (x)}
	 *
	 * <p>If the value {@code x} is convertible to an integer, returns that integer.
	 * Otherwise, returns <b>nil</b>.</p>
	 *
	 * @return the {@code math.tointeger} function
	*/
	public abstract LuaFunction _tointeger();

	/**
	 * {@code math.type (x)}
	 *
	 * <p>Returns {@code "integer"} if {@code x} is an integer, {@code "float"} if it is a float,
	 * or <b>nil</b> if {@code x} is not a number.</p>
	 *
	 * @return the {@code math.type} function
	 */
	public abstract LuaFunction _type();

	/**
	 * {@code math.ult (m, n)}
	 *
	 * <p>Returns a boolean, <b>true</b> if integer {@code m} is below integer {@code n} when
	 * they are compared as unsigned integers.</p>
	 *
	 * @return the {@code math.ult} function
	 */
	public abstract LuaFunction _ult();

}
