package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.BitField;

/**
 * an interface to our ALU design.
 *
 * s0 and s1 are the operation:
 * 00 : ADD
 * 01 : OR
 * 10 : AND
 * 11 : XOR
 *
 * cIn : carryIn
 * bInv : invert the b input (useful for doing 1's complement subtraction)
 *
 * result will contain the result of the operation and carryOut, overflow flags
 *
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/15/20
 */
public interface ALU {

    /**
     * send a full field operation to the ALU. Method will block until a result is ready
     *
     * @param s0 operation 0
     * @param s1 operation 1
     * @param cIn carry in
     * @param bInv invert B
     * @param a a field
     * @param b b field
     * @return the result and ALU operation flags
     */
    ALUResult operate(boolean s0, boolean s1, boolean cIn, boolean bInv, BitField a, BitField b);
}
