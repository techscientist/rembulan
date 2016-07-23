package net.sandius.rembulan.lbc.recompiler.gen.block;

import net.sandius.rembulan.compiler.analysis.NumericOperationType;
import net.sandius.rembulan.compiler.analysis.StaticMathImplementation;
import net.sandius.rembulan.lbc.recompiler.gen.CodeVisitor;
import net.sandius.rembulan.lbc.recompiler.gen.Origin;
import net.sandius.rembulan.lbc.recompiler.gen.PrototypeContext;
import net.sandius.rembulan.lbc.recompiler.gen.Slot;
import net.sandius.rembulan.lbc.recompiler.gen.SlotState;
import net.sandius.rembulan.util.Check;

import static net.sandius.rembulan.compiler.analysis.StaticMathImplementation.MAY_BE_INTEGER;
import static net.sandius.rembulan.compiler.analysis.StaticMathImplementation.MUST_BE_FLOAT;
import static net.sandius.rembulan.compiler.analysis.StaticMathImplementation.MUST_BE_INTEGER;

public class LuaBinaryOperation extends Linear implements LuaInstruction {

	public enum Op {
		ADD, SUB, MUL, MOD, POW, DIV, IDIV, BAND, BOR, BXOR, SHL, SHR
	}

	public final PrototypeContext context;

	public final Op op;

	public final int r_dest;
	public final int rk_left;
	public final int rk_right;

	public LuaBinaryOperation(PrototypeContext context, Op op, int a, int b, int c) {
		this.context = Check.notNull(context);
		this.op = Check.notNull(op);
		this.r_dest = a;
		this.rk_left = LuaUtils.registerOrConst(b);
		this.rk_right = LuaUtils.registerOrConst(c);
	}

	public static StaticMathImplementation mathForOp(Op op) {
		switch (op) {
			case ADD:  return MAY_BE_INTEGER;
			case SUB:  return MAY_BE_INTEGER;
			case MUL:  return MAY_BE_INTEGER;
			case MOD:  return MAY_BE_INTEGER;
			case POW:  return MUST_BE_FLOAT;
			case DIV:  return MUST_BE_FLOAT;
			case IDIV: return MAY_BE_INTEGER;
			case BAND: return MUST_BE_INTEGER;
			case BOR:  return MUST_BE_INTEGER;
			case BXOR: return MUST_BE_INTEGER;
			case SHL:  return MUST_BE_INTEGER;
			case SHR:  return MUST_BE_INTEGER;
			default:   throw new IllegalArgumentException("Illegal operation: " + op);
		}
	}
	
	@Override
	public String toString() {
		return op.name() + LuaUtils.numOpTypeToSuffix(opType(inSlots())) + "(" + r_dest + "," + rk_left + "," + rk_right + ")";
	}

	protected NumericOperationType opType(SlotState s) {
		return mathForOp(op).opType(LuaUtils.slotType(context, s, rk_left), LuaUtils.slotType(context, s, rk_right));
	}

	@Override
	protected SlotState effect(SlotState s) {
		return s.update(r_dest, Slot.of(Origin.Computed.in(this), opType(s).toType()));
	}

	@Override
	public void emit(CodeVisitor visitor) {
		switch (op) {
			case ADD:  visitor.visitAdd(this, inSlots(), r_dest, rk_left, rk_right); break;
			case SUB:  visitor.visitSub(this, inSlots(), r_dest, rk_left, rk_right); break;
			case MUL:  visitor.visitMul(this, inSlots(), r_dest, rk_left, rk_right); break;
			case MOD:  visitor.visitMod(this, inSlots(), r_dest, rk_left, rk_right); break;
			case POW:  visitor.visitPow(this, inSlots(), r_dest, rk_left, rk_right); break;
			case DIV:  visitor.visitDiv(this, inSlots(), r_dest, rk_left, rk_right); break;
			case IDIV: visitor.visitIDiv(this, inSlots(), r_dest, rk_left, rk_right); break;
			case BAND: visitor.visitBAnd(this, inSlots(), r_dest, rk_left, rk_right); break;
			case BOR:  visitor.visitBOr(this, inSlots(), r_dest, rk_left, rk_right); break;
			case BXOR: visitor.visitBXOr(this, inSlots(), r_dest, rk_left, rk_right); break;
			case SHL:  visitor.visitShl(this, inSlots(), r_dest, rk_left, rk_right); break;
			case SHR:  visitor.visitShr(this, inSlots(), r_dest, rk_left, rk_right); break;
		}
	}

}