package fasar.ingredienty.common.dao;

/**
 * Created by fabien_s on 10/08/2014.
 */
public interface DaoGeneric<E, ID> {

    public E getById(ID id);

    public Iterable<E> findAll(int nbElement, int page, boolean ascending);

    public boolean update(E element);

    public ID insert(E element);

    public boolean removeById(ID id);

    public boolean remove(E element);

}
