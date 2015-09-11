import java.util.Map;
import java.util.Comparator;



public class Custom_Comparator implements Comparator<Object> {
 
  Map<String, Double> map;
 
  public Custom_Comparator(Map<String,Double> map){
    this.map = map;
  }
  public int compare(Object token1, Object token2){
 
    Comparable<Double> frequency1 = (Comparable<Double>) map.get(token1);
    Comparable<Double> frequency2 = (Comparable) map.get(token2);
    
   // return valueB.compareTo((Integer) valueA);
    
    if((Double)frequency1 > (Double)frequency2){
		return -1;
	}	
   return 1;
 
  }
	
	
	
	 
}