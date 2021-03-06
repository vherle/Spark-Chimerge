package SparkTesting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.clearspring.analytics.util.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 * This represents one block of data.
 * This will be one unit of data. 
 */

public class Block implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	Map<Double, List<AttributeLabelPair>> map = Maps.newHashMap();

	public Block(List<AttributeLabelPair> records, Double id) {
		this.map.put(id, records);
	}

	private Block(Map<Double, List<AttributeLabelPair>> map) {
		this.map = map;
	}
	
	public List<AttributeLabelPair> getRecordsForId(Double id) {
		return map.get(id);
	}

	public Set<Double> getIds() {
		return map.keySet();
	}
	
	@Override
	public String toString() {
		return "Blockie [values = " + getIds() + "]";
	}

	public boolean contains(Block b) {
		return this.map.keySet().containsAll(b.getMap().keySet());
	}

	public Map<Double, List<AttributeLabelPair>> getMap() {
		return map;
	}
	
	public BigDecimal getFingerPrint(){
		BigDecimal fingerPrint = BigDecimal.ZERO;
		for(Double d: getIds()) {
			fingerPrint = fingerPrint.add(BigDecimal.valueOf(d));
		}
		return fingerPrint.divide(BigDecimal.valueOf(getIds().size()), RoundingMode.HALF_EVEN);
	}
	
	public Block merge(Block b) {
		Map<Double, List<AttributeLabelPair>> map = Maps.newHashMap();
		map.putAll(this.map);
		map.putAll(b.getMap());
		return new Block(map);
	}
	
	public boolean overlaps(Block b) {
		return !Sets.intersection(this.getIds(), b.getIds()).isEmpty();
	}
	
	public List<AttributeLabelPair> getAllRecords() {
		List<AttributeLabelPair> records = Lists.newArrayList();
		for(List<AttributeLabelPair> list: map.values()){
			records.addAll(list);
		}
		return records;
	}
	
	public Double getMaxRange() {
		List<Double> keys = Lists.newArrayList(map.keySet());
		Collections.sort(keys);
		return keys.get(keys.size() - 1);
	}
	
	public Double getMinRange() {
		List<Double> keys = Lists.newArrayList(map.keySet());
		Collections.sort(keys);
		return keys.get(0);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (map == null) {
			if (other.map != null)
				return false;
		} else if (!map.equals(other.map))
			return false;
		return true;
	}
}