# RGB Streams

 Heavily inspired by:
 * https://codegolf.stackexchange.com/questions/35569/tweetable-mathematical-art
 * https://codegolf.stackexchange.com/questions/22144/images-with-all-colors

Goals:
* Make the code easier to extend to any drawing by factoring points generation, rendering,
 and saving to a file, so that concrete classes only need to implement the RGB computation
* Make it work transparently for big images
* Test parallel streams speedups in various cases
 
Times are measured on an old 2-cores machine
