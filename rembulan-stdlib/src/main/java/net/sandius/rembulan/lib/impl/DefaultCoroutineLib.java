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

package net.sandius.rembulan.lib.impl;

import net.sandius.rembulan.lib.CoroutineLib;
import net.sandius.rembulan.runtime.AbstractFunctionAnyArg;
import net.sandius.rembulan.runtime.Coroutine;
import net.sandius.rembulan.runtime.ExecutionContext;
import net.sandius.rembulan.runtime.LuaFunction;
import net.sandius.rembulan.runtime.ProtectedResumable;
import net.sandius.rembulan.runtime.ResolvedControlThrowable;
import net.sandius.rembulan.runtime.ReturnBuffer;
import net.sandius.rembulan.runtime.UnresolvedControlThrowable;
import net.sandius.rembulan.util.Check;

import java.util.ArrayList;
import java.util.Arrays;

public class DefaultCoroutineLib extends CoroutineLib {

	@Override
	protected LuaFunction _create() {
		return Create.INSTANCE;
	}

	@Override
	protected LuaFunction _resume() {
		return Resume.INSTANCE;
	}

	@Override
	protected LuaFunction _yield() {
		return Yield.INSTANCE;
	}

	@Override
	protected LuaFunction _isyieldable() {
		return IsYieldable.INSTANCE;
	}

	@Override
	protected LuaFunction _status() {
		return Status.INSTANCE;
	}

	@Override
	protected LuaFunction _running() {
		return Running.INSTANCE;
	}

	@Override
	protected LuaFunction _wrap() {
		return Wrap.INSTANCE;
	}

	public static class Create extends AbstractLibFunction {

		public static final Create INSTANCE = new Create();

		@Override
		protected String name() {
			return "create";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			LuaFunction func = args.nextFunction();
			Coroutine c = context.newCoroutine(func);
			context.getReturnBuffer().setTo(c);
		}

	}

	public static class Resume extends AbstractLibFunction implements ProtectedResumable {

		public static final Resume INSTANCE = new Resume();

		@Override
		protected String name() {
			return "resume";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			Coroutine coroutine = args.nextCoroutine();
			Object[] resumeArgs = args.getTail();

			context.getReturnBuffer().setTo();

			try {
				context.resume(coroutine, resumeArgs);
			}
			catch (UnresolvedControlThrowable ct) {
				throw ct.resolve(this, null);
			}
		}

		@Override
		public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
			ReturnBuffer rbuf = context.getReturnBuffer();
			ArrayList<Object> result = new ArrayList<>();
			result.add(Boolean.TRUE);
			result.addAll(Arrays.asList(rbuf.getAsArray()));
			rbuf.setToContentsOf(result);
		}

		@Override
		public void resumeError(ExecutionContext context, Object suspendedState, Object error) throws ResolvedControlThrowable {
			context.getReturnBuffer().setTo(Boolean.FALSE, error);
		}

	}

	public static class Yield extends AbstractLibFunction {

		public static final Yield INSTANCE = new Yield();

		@Override
		protected String name() {
			return "yield";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			try {
				context.yield(args.getAll());
			}
			catch (UnresolvedControlThrowable ct) {
				throw ct.resolve(this, null);
			}
		}

		@Override
		public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
			// no-op, the resume arguments are on the stack already
		}

	}

	public static class IsYieldable extends AbstractLibFunction {

		public static final IsYieldable INSTANCE = new IsYieldable();

		@Override
		protected String name() {
			return "isyieldable";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			context.getReturnBuffer().setTo(context.isInMainCoroutine());
		}

	}

	public static class Status extends AbstractLibFunction {

		public static final Status INSTANCE = new Status();

		public static final String STATUS_RUNNING = "running";
		public static final String STATUS_SUSPENDED = "suspended";
		public static final String STATUS_NORMAL = "normal";
		public static final String STATUS_DEAD = "dead";

		public static String status(ExecutionContext context, Coroutine coroutine) {
			switch (context.getCoroutineStatus(coroutine)) {
				case SUSPENDED:  return STATUS_SUSPENDED;
				case RUNNING:    return STATUS_RUNNING;
				case NORMAL:     return STATUS_NORMAL;
				case DEAD:       return STATUS_DEAD;
			}
			throw new AssertionError();  // should never happen
		}

		@Override
		protected String name() {
			return "status";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			Coroutine coroutine = args.nextCoroutine();
			context.getReturnBuffer().setTo(status(context, coroutine));
		}

	}

	public static class Running extends AbstractLibFunction {

		public static final Running INSTANCE = new Running();

		@Override
		protected String name() {
			return "running";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			Coroutine c = context.getCurrentCoroutine();
			context.getReturnBuffer().setTo(c, !context.isInMainCoroutine());
		}

	}

	public static class Wrap extends AbstractLibFunction {

		public static final Wrap INSTANCE = new Wrap();

		static class WrappedCoroutine extends AbstractFunctionAnyArg {

			private final Coroutine coroutine;

			public WrappedCoroutine(LuaFunction function, ExecutionContext context) {
				Check.notNull(function);
				Check.notNull(context);
				this.coroutine = context.newCoroutine(function);
			}

			@Override
			public void invoke(ExecutionContext context, Object[] args) throws ResolvedControlThrowable {
				context.getReturnBuffer().setTo();
				try {
					context.resume(coroutine, args);
				}
				catch (UnresolvedControlThrowable ct) {
					throw ct.resolve(this, null);
				}
			}

			@Override
			public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
				// no-op
			}

		}

		@Override
		protected String name() {
			return "wrap";
		}

		@Override
		protected void invoke(ExecutionContext context, ArgumentIterator args) throws ResolvedControlThrowable {
			LuaFunction f = args.nextFunction();
			LuaFunction result = new WrappedCoroutine(f, context);
			context.getReturnBuffer().setTo(result);
		}

	}

}
