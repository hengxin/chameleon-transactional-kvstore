package kvs.compound;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import client.clientlibrary.transaction.RVSITransaction;
import kvs.component.Ordinal;
import net.jcip.annotations.ThreadSafe;

/**
 * An index from {@link CompoundKey} to {@link Ordinal}.
 * <p>
 * (For now, it is only accessed in {@link RVSITransaction#commit()}.
 * If it is used elsewhere later, pay attention to the synchronization issues.)
 * 
 * @author hengxin
 * @date Created on Dec 11, 2015
 */
@ThreadSafe
public final class CKeyToOrdinalIndex
{
	private final ConcurrentMap<CompoundKey, Ordinal> ckeyts_ord_map = new ConcurrentHashMap<>();

	/**
	 * Return the {@link Ordinal} value associated with the {@link CompoundKey} if it already exists
	 * in the {@link #ckeyts_ord_map}, or first <i>put</i> the default {@value Ordinal#ORDINAL_INIT}
	 * value to it and then return the default {@value Ordinal#ORDINAL_INIT} if {@link #ckeyts_ord_map}
	 * contains no mapping for the specified {@link CompoundKey}.
	 * 
	 * @param ck
	 * 	{@link CompoundKey} specified to search for
	 * @return
	 * 	The already existing {@link Ordinal} associated with the specified key, or {@value Ordinal#ORDINAL_INIT}.
	 */
	public Ordinal get(CompoundKey ck)
	{
		return this.ckeyts_ord_map.computeIfAbsent(ck, k -> {return Ordinal.ORDINAL_INIT;});
	}
}

