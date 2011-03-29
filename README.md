# Pacer

Pacer is a JRuby library that enables very expressive graph traversals.

It currently supports 2 major graph database: [Neo4j](http://neo4j.org) and [Dex](http://www.sparsity-technologies.com/dex) using the [Tinkerpop](http://tinkerpop.com) graphdb stack. Plus there's a very convenient in-memory graph called TinkerGraph which is part of [Blueprints](http://blueprints.tinkerpop.com).

Pacer allows you to create, modify and traverse graphs using very fast and memory efficient stream processing thanks to the very cool [Pipes](http://pipes.tinkerpop.com) library. That also means that almost all processing is done in pure Java, so when it comes the usual Ruby expressiveness vs. speed problem, you can have your cake and eat it too, it's very fast!

## Example traversals


Friend recommendation algorithm expressed in basic traversal functions:

    friends = person.out_e(:friend).in_v(:type => 'person')
    friends.out_e(:friend).in_v(:type => 'person').except(friends).except(person).most_frequent(0...10)

or using Pacer's route extensions to create your own query methods:

    person.friends.friends.except(person.friends).except(person).most_frequent(0...10)

or to take it one step further:

    person.recommended_friends



## Create and populate a graph

To get started, you need to know just a few methods. First, open up a graph (if one doesn't exist it will be automatically created) and add some vertices to it:

    dex = Pacer.dex '/tmp/dex_demo'
    pangloss = dex.create_vertex :name => 'pangloss', :type => 'user'
    okram = dex.create_vertex :name => 'okram', :type => 'user'
    group = dex.create_vertex :name => 'Tinkerpop', :type => 'group'


Now, let's see what we've got:

    dex.v

produces:

    #<V[1024]> #<V[1025]> #<V[1026]>
    Total: 3
    => #<GraphV>

There are our vertices. Let's look their properties:

    dex.v.properties

    {"name"=>"pangloss", "type"=>"user"} {"name"=>"okram", "type"=>"user"}
    {"name"=>"Tinkerpop", "type"=>"group"}
    Total: 3
    => #<GraphV -> Obj-Map>

Now let's put an edge between them:

    dex.create_edge okram, pangloss, :inspired
    => #<E[2048]:1025-inspired-1024>

That's great for creating an edge but what if I've got lots to create? Try this method instead which can add edges to the cross product of all vertices in one route with all vertices in the other:

    group.add_edges_to :member, dex.v(:type => 'user')

    #<E[4097]:1026-member-1024> #<E[4098]:1026-member-1025>
    Total: 2
    => #<Obj 2 ids -> lookup>

There is plenty more to see as well! Please dig into the code and the spec suite to find loads of examples and edge cases. And if you think of a case that I've missed, I'll greatly appreciate your contributions!

## Design Philosophy

I want Pacer and its ecosystem to become a repository for real implementations of ideas, best practices and techniques for streaming data manipulation. I've got lots of ideas that I'd like to add, and although Pacer seems to be quite rock solid right now -- and I am using it in limited production environments -- it is still in flux. If we find a better way to do something, we're going to do it that way even if that means breaking changes from one release to another.

Once Pacer matures further, a decision will be made to 'lock it down' at least a little more, hopefully there will be a community in place by then to help determine the right time for that to happen!

Pacer is meant to be extensible and pluggable. If you look at any file in the filter/ side_effect/ or transform/ folders, you'll see that they add features to Pacer in a completely self-contained way. If you want to add a traversal technique to Pacer, you can fork Pacer and send me a pull request or just create your own pacer-<feature name> plugin! I will be releasing some of those as well in the near future.

## Pluggable Architecture

Pacer is built on a very modular architecture and nearly every chainable route method is actually implemented in a pluggable module. See the lib/pacer/filter folder for a handful of examples that vary widely in complexity.

To see how to build your own Pacer plugin, see my example plugin at https://github.com/pangloss/pacer-bloomfilter which also has a readme file that goes into considerable detail on the process of creating plugins and provides some additional usage examples as well.

## Gremlin


If you're already familiar with [Gremlin](http://gremlin.tinkerpop.com), please look at my [Introducing Pacer](http://ofallpossibleworlds.wordpress.com/2010/12/19/introducing-pacer) post for a simple introduction and explanation of how Pacer is at once similar to and quite different from Gremlin, the project that inspired it. That post is a little out of date at this point since it refers to the original version of Gremlin. Groovy Gremlin is the latest version, inspired in turn by Pacer!

## Test Coverage

I'm aiming for 100% test coverage in Pacer and am currently nearly there in the core classes, but there is a way to go with the filter, transform and side effect route modules. Open coverage/index.html to see the current state of test coverage. And of course contributions would be much apreciated.
