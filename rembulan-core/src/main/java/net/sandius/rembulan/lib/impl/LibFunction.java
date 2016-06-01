package net.sandius.rembulan.lib.impl;

import net.sandius.rembulan.core.ControlThrowable;
import net.sandius.rembulan.core.Conversions;
import net.sandius.rembulan.core.Coroutine;
import net.sandius.rembulan.core.ExecutionContext;
import net.sandius.rembulan.core.Function;
import net.sandius.rembulan.core.LNumber;
import net.sandius.rembulan.core.NonsuspendableFunctionException;
import net.sandius.rembulan.core.Table;
import net.sandius.rembulan.core.ValueTypeNamer;
import net.sandius.rembulan.core.impl.FunctionAnyarg;
import net.sandius.rembulan.core.impl.Varargs;
import net.sandius.rembulan.lib.LibUtils;
import net.sandius.rembulan.util.Check;

public abstract class LibFunction extends FunctionAnyarg {

	public static class CallArguments {

		private final ValueTypeNamer namer;

		public final String name;
		public final Object[] args;
		public int index;

		public CallArguments(ValueTypeNamer namer, String name, Object[] args) {
			this.namer = Check.notNull(namer);
			this.name = Check.notNull(name);
			this.args = Check.notNull(args);
			this.index = 0;
		}

		public ValueTypeNamer namer() {
			return namer;
		}

		public void reset() {
			index = 0;
		}

		public void skip() {
			index += 1;
		}

		public boolean hasNext() {
			return index < args.length;
		}

		public int size() {
			return args.length;
		}

		public int tailSize() {
			return Math.max(args.length - index, 0);
		}

		public Object[] getAll() {
			return args;
		}

		public Object[] getTail() {
			return Varargs.from(args, index);
		}

		public Object nextAny() {
			return LibUtils.checkValue(name, args, index++);
		}

		public Object optNextAny() {
			return Varargs.getElement(args, index++);
		}

		public int nextInt() {
			return LibUtils.checkInt(namer, name, args, index++);
		}

		public int optNextInt(int defaultValue) {
			if (hasNext()) {
				if (args[index] == null) {
					index += 1;
					return defaultValue;
				}
				else {
					return nextInt();
				}
			}
			else {
				return defaultValue;
			}
		}

		public int nextIntRange(String rangeName, int min, int max) {
			return LibUtils.checkRange(namer, name, args, index++, rangeName, min, max);
		}

		public LNumber nextNumber() {
			return LibUtils.checkNumber(namer, name, args, index++);
		}

		public long nextInteger() {
			return LibUtils.checkInteger(namer, name, args, index++);
		}

		public boolean optNextBoolean(boolean defaultValue) {
			if (hasNext()) {
				return Conversions.objectToBoolean(nextAny());
			}
			else {
				return defaultValue;
			}
		}

		public String nextString() {
			return LibUtils.checkString(namer, name, args, index++);
		}

		public String nextStrictString() {
			return LibUtils.checkString(namer, name, args, index++, true);
		}

		public String optNextString(String defaultValue) {
			if (hasNext()) {
				return nextString();
			}
			else {
				return defaultValue;
			}
		}

		public Function nextFunction() {
			return LibUtils.checkFunction(namer, name, args, index++);
		}

		public Table nextTable() {
			return LibUtils.checkTable(namer, name, args, index++);
		}

		public Table nextTableOrNil() {
			return LibUtils.checkTableOrNil(name, args, index++);
		}

		public Table optNextTable() {
			if (hasNext()) {
				if (args[index] == null) {
					index += 1;
					return null;
				}
				else {
					return LibUtils.checkTable(namer, name, args, index++);
				}
			}
			else {
				return null;
			}
		}

		public Coroutine nextCoroutine() {
			return LibUtils.checkCoroutine(namer, name, args, index++);
		}

	}

	protected abstract String name();

	@Override
	public void invoke(ExecutionContext context, Object[] args) throws ControlThrowable {
		CallArguments callArgs = new CallArguments(new LibUtils.NameMetamethodValueTypeNamer(context.getState()), name(), args);
		invoke(context, callArgs);
	}

	protected abstract void invoke(ExecutionContext context, CallArguments args) throws ControlThrowable;

	@Override
	public void resume(ExecutionContext context, Object suspendedState) throws ControlThrowable {
		throw new NonsuspendableFunctionException(this.getClass());
	}

}
