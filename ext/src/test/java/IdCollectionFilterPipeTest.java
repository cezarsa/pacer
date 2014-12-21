package com.xnlogic.pacer.pipes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import com.tinkerpop.blueprints.Contains;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import java.util.Arrays;
import java.util.NoSuchElementException;
import com.xnlogic.pacer.pipes.IdCollectionFilterPipe;

public class IdCollectionFilterPipeTest {
    private TinkerGraph graph = null;

    @Before
    public void setup() throws Exception {
        this.graph = new TinkerGraph();
    }

    @After
    public void teardown() throws Exception {
        this.graph.shutdown();
        this.graph = null;
    }
    
    @Test(expected=NoSuchElementException.class)
    public void containsInTest() {
        IdCollectionFilterPipe<Vertex> idCollectionFilterPipe = new IdCollectionFilterPipe(Contains.IN, "1", "2", "3", "4");

        Vertex v1 = this.graph.addVertex("1");
        Vertex v2 = this.graph.addVertex("2");
        Vertex v3 = this.graph.addVertex("5");

        idCollectionFilterPipe.setStarts(Arrays.asList(v1, v2, v3));

        Vertex v = idCollectionFilterPipe.next();
        assertTrue(v.getId().equals("1"));
        
        v = idCollectionFilterPipe.next();
        assertTrue(v.getId().equals("2"));

        v = idCollectionFilterPipe.next();
    }

    @Test(expected=NoSuchElementException.class)
    public void containsNotInTest() {
        IdCollectionFilterPipe<Vertex> idCollectionFilterPipe = new IdCollectionFilterPipe(Contains.NOT_IN, "1", "2", "3", "4");

        Vertex v1 = this.graph.addVertex("7");
        Vertex v2 = this.graph.addVertex("8");
        Vertex v3 = this.graph.addVertex("1");

        idCollectionFilterPipe.setStarts(Arrays.asList(v1, v2, v3));

        Vertex v = idCollectionFilterPipe.next();
        assertTrue(v.getId().equals("7"));
        
        v = idCollectionFilterPipe.next();
        assertTrue(v.getId().equals("8"));

        v = idCollectionFilterPipe.next();
    }

    // TODO: Lookup "Contains" and see if there are more than just the two values in the enum.
}
