
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
import random as r

def function(x):
    return x*np.sin(10*np.pi*x)+2.0
def plots(x2,g_best,g_best_val):
    x1=np.linspace(-1,2,1000)
    plt.plot(x1,function(x1))
    y=[]
    for i in range(len(x2)):
        y.append(function(x2[i]))
    plt.plot(x2,y,'bo')
    y=np.array(y)
    k=plt.plot(g_best,g_best_val,'r*')
def PSO_min(n_particle,c1,c2,w,n_iteration):
    y=np.random.uniform(-1,2,size=(n_particle,1))
    V=np.zeros((n_particle,1))
    pbest=y
    pbest_val=function(y)
    g_best_val=pbest_val.min()
    g_best=pbest[pbest_val.argmin()]
    plt.plot(g_best,g_best_val,'r*')
    r=[np.random.uniform(2),np.random.uniform(2)]
    
    for i in range(n_iteration):
        V=w*V+c1*r[0]*(pbest-y)+c2*r[1]*(g_best-y)
        y=y+V
        val=function(y)
        pbest=y
        pbest_val=val
        if(pbest_val.min()<g_best_val):
            g_best_val=pbest_val.min()
            g_best=pbest[pbest_val.argmin()]
        plots(y,g_best,g_best_val)
        plt.figure()
PSO_min(10,0.1,0.1,0.5,20)
def PSO_max(n_particle,c1,c2,w,n_iteration):
    y=np.random.uniform(-1,2,size=(n_particle,1))
    V=np.zeros((n_particle,1))
    pbest=y
    pbest_val=function(y)
    g_best_val=pbest_val.max()
    g_best=pbest[pbest_val.argmax()]
    plt.plot(g_best,g_best_val,'r*')
    r=[np.random.uniform(2),np.random.uniform(2)]
    
    for i in range(n_iteration):
        V=w*V+c1*r[0]*(pbest-y)+c2*r[1]*(g_best-y)
        y=y+V
        val=function(y)
        pbest=y
        pbest_val=val
        if(pbest_val.min()<g_best_val):
            g_best_val=pbest_val.max()
            g_best=pbest[pbest_val.argmax()]
        plots(y,g_best,g_best_val)
        plt.figure()
PSO_max(100,0.1,0.1,0.5,20)
