package fasar.ingredienty.common.doa;

/**
 * Created by fabien_s on 10/08/2014.
 */
public interface DoaAbstract<E> {

    public Iterable<E> findAll();

    public boolean update(E element);

    public boolean insert(E element);

    public boolean remove(E element);

}
