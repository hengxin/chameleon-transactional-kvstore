package kvs.compound;

import com.google.common.base.MoreObjects;

import net.jcip.annotations.ThreadSafe;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import client.clientlibrary.transaction.RVSITransaction;
import kvs.component.Ordinal;

/**
 * An index from {@link CompoundKey} to {@link Ordinal}.
 * <p>
 * (For now, it is only accessed in {@link RVSITransaction}.
 * If it is used elsewhere later, pay attention to the synchronization issues.)
 * 
 * @author hengxin
 * @date Created on Dec 11, 2015
 */
@ThreadSafe
public final class CKeyToOrdinal {
	private final ConcurrentMap<CompoundKey, Ordinal> ckeytsOrdMap = new ConcurrentHashMap<>();

	/**
	 * Return the {@link Ordinal} value associated with the {@link CompoundKey} if it already exists
	 * in the {@link #ckeytsOrdMap}, or first <i>put</i> the default {@link Ordinal#ORDINAL_INIT}
	 * value to it and then return the default {@link Ordinal#ORDINAL_INIT} if {@link #ckeytsOrdMap}
	 * contains no mapping for the specified {@link CompoundKey}.
	 * 
	 * @param ck {@link CompoundKey} to look up
	 * @return	the {@link Ordinal} associated with {@code ck}, or {@link Ordinal#ORDINAL_INIT}
	 */
	@NotNull
	public Ordinal lookup(CompoundKey ck) {
		return ckeytsOrdMap.computeIfAbsent(ck, k -> Ordinal.ORDINAL_INIT());
	}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ckeytsOrdMap", ckeytsOrdMap)
                .toString();
    }

}

