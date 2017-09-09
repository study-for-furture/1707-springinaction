package com.chap05.spittr.data;

import com.chap05.spittr.Spittle;

import java.util.List;

/**
 * Created by mitmo on 2017-09-03.
 */
public interface SpittleRepository {
    List<Spittle> findSpittles(long max, int count);

    List<Spittle> findRecentSpittles();

    Spittle findOne(long id);

    void save(Spittle spittle);
}
