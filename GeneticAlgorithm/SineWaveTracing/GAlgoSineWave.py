
import matplotlib.pyplot as plt
import numpy as np
import random as r
import pandas as pd

x=np.linspace(-1,2,1000)
y=x*(np.sin(10*np.pi*x))+2
plt.plot(x,y)


def fitness(x):
    return x*np.sin(10*np.pi*x)+2.0



def init(popsize,resolution):
    population=[]
    for i in range(0,popsize):
        k1=[]
        sl=[]
        for j in range(0,resolution):
            k=r.randint(0,1)
            k1.append(k)
        if(k1 not in population):
            sl.append(k1)
            sl.append(fitness(bin_decode(k1)))
            population.append(sl)
    return np.array(population,dtype=object)


def bin_decode(chromosome):
    k=0
    for i in range(len(chromosome)):
        if(chromosome[i]==1):
            k+=pow(2,i)
    decoded=((k/pow(2,len(chromosome)))*(2+1))-1
    #print(decoded,fitness(decoded))
    return decoded


def crossover(popset1):
    popset=list(popset1)
    #print(len(popset))
    idx1=int(len(popset)/2)
    #idx2=r.randint(3,len(popset)-1)
    parent1=r.randint(0,len(popset)-1)
    parent2=r.randint(0,len(popset)-1)
    c1,c2=popset[parent1][0][:idx1]+popset[parent2][0][idx1:],popset[parent1][0][idx1:]+popset[parent2][0][:idx1]
    sl=[]
    sl.append(c1)
    sl.append(fitness(bin_decode(c1)))
    #print(sl)
   # np.append(popset,sl)
    
    popset.append(sl)
    
    sl.clear()
    sl.append(c2)
    sl.append(fitness(bin_decode(c2)))
    #print(sl)
    
    popset.append(sl)
    
   # popset=np.append(popset,sl)
    #popset+=np.array(sl,dtype=object)
    #print(len(popset3))
    
    return np.array(popset)


def mutation(chromosome):
    idx=r.randint(0,len(chromosome)-1)
    chromosome[idx]=int(not chromosome[idx])
    return chromosome



def selection(popset,selection_rate):
    sel_len=int(len(popset)*selection_rate)
    p1=list(popset)
    #p2=p1[0:sel_len//2]+p1[-sel_len//2:]
    p2=p1[0:sel_len//2]
    return np.array(p2)

def plot(poplist):
    plt_pt=[i[-1] for i in poplist]

    pl_x=[]
    for i in range(len(poplist)):
        pl_x.append(bin_decode(poplist[i][0]))
    x1=np.linspace(-1,2,1000)
    y=x1*(np.sin(10*np.pi*x1))+2
    plt.plot(x1,y)
    plt.plot(pl_x,plt_pt,'x')

def rem_dup(x):
    k1=[]
    k2=[]
    '''
    for k in range(len(x)):
        k1.append(tuple(x[:,0][k]))
    #k=(x[:,0][0])
    l=list(set(k1))
    for i in range(len(l)):
        print(l[i],bin_decode(l[i]),fitness(bin_decode(l[i])))
    '''
    for i in range(len(x)):
        k1.append(tuple(x[i][0]))
    #print(set(k1))
    k1=list(set(k1))
    for j in range(len(k1)):
        sl=[]
        sl.append(list(k1[j]))
        sl.append(fitness(bin_decode(k1[j])))
        k2.append(sl)
    return np.array(k2,dtype=object)

def GA_simulate(mutation_rate,crossover_rate,selection_rate,resolution,popsize,n_generations):
    x=init(popsize,resolution)
    for i in range(n_generations):
        
        for j in range(int(crossover_rate*len(x))):
            x=crossover(x)
        x.sort(axis=0)
        for k in range(int(mutation_rate*len(x))):
            mutation(x[0][0])
            x[0][1]=fitness(bin_decode(x[0][0]))
       
        x.sort(axis=0)
        x=selection(x,selection_rate)
        plt.figure()
        x=rem_dup(x)
        plot(x)
        print("##########\n")
        print(x)

GA_simulate(0.01,0.8,0.9,12,1000,10)


