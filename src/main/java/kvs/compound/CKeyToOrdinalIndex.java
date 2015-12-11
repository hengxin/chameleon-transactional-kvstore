package kvs.compound;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
	
	public Ordinal get(CompoundKey ck)
	{
		return this.ckeyts_ord_map.getOrDefault(ck, Ordinal.ORDINAL_INIT);
	}
	
	public void put(CompoundKey ck, Ordinal ord)
	{
		this.ckeyts_ord_map.putIfAbsent(ck, ord);
	}
}
