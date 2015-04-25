An Analysis of Variation Methods used in Genetic Algorithms
===========================================================

This program was created as a part of an independent research course on 
Evolutionary Computation taken in the spring of 2015. The inspiration for this 
program is from chapter 2.2 of the text "Introduction to Evolutionary 
Algorithms" by Xinjie Yu.

This simple genetic algorithm is used to attempt to find a solution to the 
following optimization problems:

### Function 1

A simple sinusoidal function with many local minima and maxima. This is
a maximum problem with a global maxima at f(1.85) = 3.85.

max f(x) = x * sin(10 * pi * x) + 2.0, 

s.t. -1 < x < 2

Dimensions: 1

The curve of the above objective function is shown below:

![Image of curve](https://raw.githubusercontent.com/bjcrawford/SGAVariationAnalysis/master/res/function1.png)

Credit: "Introduction to Evolutionary Algorithms" by [Xinjie Yu](http://www.tsinghua.edu.cn/publish/eeaen/1699/2010/20101216114256922137685/20101216114256922137685_.html), Chapter 2

### Function 2

The sphere model function (De Jong's Function 1) is convex, continuous,
and unimodal. This is a minimum problem with a global minimum at 
f(0) = 0.0.
 
min f(x) = sum(xi^2), 
 
s.t. -5 < x < 5

Dimensions: 5

The 2 dimensional plot of the above objective function is shown below:

![Image of curve](https://raw.githubusercontent.com/bjcrawford/SGAVariationAnalysis/master/res/function2.png)

Credit: [Virtual Library of Simulation Experiments](http://www.sfu.ca/~ssurjano/spheref.html)

### Function 3

The Ackley function offers many local minima on the outer edges
with a deep global minima located at the center. This is a minimum 
problem with a global minimum at f(0) = 0.0.
 
min f(x) = -20 * exp(-0.2 * sqrt(1/n * sum(xi^2)) - exp(1/n * sum(cos(2 * pi * xi))) + 20 + e, 
            
s.t. -20 < x < 30
 
Dimensions: 2

![Image of curve](https://raw.githubusercontent.com/bjcrawford/SGAVariationAnalysis/master/res/function3.png)

Credit: [Virtual Library of Simulation Experiments](http://www.sfu.ca/~ssurjano/ackley.html)