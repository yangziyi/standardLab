package lab.base.util;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class developUtil {
    /**
     * 取两个set的交集
     */
    public Set<Object> intersectionSet(Set<Object> set1, Set<Object> set2){
        Set<Object> set = new HashSet<Object>();
        set.addAll(set1);
        set.retainAll(set2);
        return set;
    }

    /**
     * 取两个set的差集
     * @param set1  更大的集合
     * @param set2  更小的集合
     * @return
     */
    public Set<Object> differenceSet(Set<Object> set1, Set<Object> set2){
        Set<Object> set = new HashSet<Object>();
        set.addAll(set1);
        set.removeAll(set2);
        return set;
    }

    /**
     * 取两个set的并集
     * @param set1
     * @param set2
     * @return
     */
    public Set<Object> unionSet(Set<Object> set1, Set<Object> set2){
        Set<Object> set = new HashSet<Object>();
        set.addAll(set1);
        set.addAll(set2);
        return set;
    }
}
