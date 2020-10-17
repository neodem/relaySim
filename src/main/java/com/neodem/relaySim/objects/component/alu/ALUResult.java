package com.neodem.relaySim.objects.component.alu;

import com.neodem.relaySim.data.bitfield.BitField;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by: Vincent Fumo (vincent_fumo@cable.comcast.com)
 * Created on: 10/15/20
 */
public class ALUResult {
    private final BitField result;
    private final boolean carryOut;
    private final boolean overflow;

    public ALUResult(BitField result) {
        this(result, false, false);
    }

    public ALUResult(BitField result, boolean carryOut) {
        this(result, carryOut, false);
    }

    public ALUResult(BitField result, boolean carryOut, boolean overflow) {
        this.result = result;
        this.carryOut = carryOut;
        this.overflow = overflow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ALUResult aluResult = (ALUResult) o;

        return new EqualsBuilder()
                .append(carryOut, aluResult.carryOut)
                .append(overflow, aluResult.overflow)
                .append(result, aluResult.result)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(result)
                .append(carryOut)
                .append(overflow)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ALUResult{" +
                "result=" + result +
                ", carryOut=" + carryOut +
                ", overflow=" + overflow +
                '}';
    }

    public BitField getResult() {
        return result;
    }

    public boolean isCarryOut() {
        return carryOut;
    }

    public boolean isOverflow() {
        return overflow;
    }
}
