# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

from keras.layers import Dropout
from keras import backend as K
from keras import layers as L
import keras as ke
import numpy as np
import tensorflow.compat.v1 as tf
import pandas as p
import matplotlib.pyplot as plt
import time as t
import gc

np.random.seed(1)
tf.compat.v1.disable_eager_execution()
tf.set_random_seed(1)


resultsFile = open("results.txt", "a+")
df=p.read_table("DataSets/cnc_twin2.txt", sep="\t", encoding="utf-8")

data=df.to_numpy()
data=np.append(data, (data[:,2]/data[:,1]).reshape(len(data),1), axis=1)
testOutputs=data[np.in1d(df['Motion'],[15, 35, 65, 85, 125, 165, 185, 225, 285]),0].reshape(-1,1)
minVal=np.repeat([[min(data[:,0]),min(data[:,1]),min(data[:,2]),min(data[:,3])]], len(data), axis=0).reshape(len(data),4)
maxVal=np.repeat([[max(data[:,0]),max(data[:,1]),max(data[:,2]),max(data[:,3])]], len(data), axis=0).reshape(len(data),4)

rangeVal=maxVal-minVal
normVal=(data-minVal)/rangeVal
testVal=normVal[np.in1d(df['Motion'],[15, 35, 65, 85, 125, 165, 185, 225, 285])]
trainingVal=normVal[np.in1d(df['Motion'],[10, 25, 50, 75, 100, 115, 135, 150, 175, 200, 215, 235, 250, 300])]
train_X = np.array(trainingVal[:,[1,2,3]])
train_Y = np.array(trainingVal[:,0]).reshape(len(trainingVal),1)
test_X = np.array(testVal[:,[1,2,3]])
test_Y = np.array(testVal[:,0]).reshape(len(testVal),1)

modelCounter=0
plotCounter=0
# objective function
def objective(params):
    mape=9999
    activations=params[0]
    nodes=params[1]
    ep=params[2]
    print(params)
    #t.sleep(5)
    #ai

    
    global modelCounter
    global plotCounter
    
    modelCounter=modelCounter+10
    cursor=0
    perfData=[]
    nSamples=5
    
    while cursor<=np.ceil(nSamples) and cursor<3: #yPerf>0.029 and
        
        sess = tf.Session()        
        K.set_session(sess)
        model = ke.Sequential()
        model.add(L.Dense(nodes[0], input_dim=3, activation=activations[0]))
        for i in range(1, len(nodes)):
            model.add(L.Dense(nodes[i], activation=activations[i]))
       
    
        
        #model.add(L.Dense(15, input_dim=3, activation='relu'))
        #model.add(L.Dense(10, activation='relu'))
        #model.add(L.Dense(5, activation='relu'))
        #model.add(L.Dense(1, activation='linear'))
        model.compile(loss='mse', optimizer='adam', metrics=['mse', 'mae'])
        
        #history = model.fit(train_X, train_Y, epochs=500000, validation_split=0.2)
        history = model.fit(train_X, train_Y, epochs=ep, 
                            validation_data=(test_X, test_Y))
        
        history_df = p.DataFrame(history.history)
        plt.figure(plotCounter)
        plt.plot(history_df['loss'], label='loss')
        plt.plot(history_df['val_loss'], label='test loss')
        #plt.legend()
        plt.savefig('J:/Loss/cnc_twin'+str(modelCounter)+'_loss.png')
        plt.close() 
        plotCounter=plotCounter+1
        
        ypred = np.array(model.predict(test_X).flatten()).reshape(len(testVal),1)
        print([x[0] for x in ypred])
        print([x[0] for x in test_Y])
        performance=sum(abs(ypred-test_Y)/test_Y)/len(test_Y)
        print("Perf:",performance)
        print([x[0] for x in abs(ypred*rangeVal[0,0]+minVal[0,0])])
        yPredVal=ypred*rangeVal[0,0]+minVal[0,0]
        
        yPerf=sum(abs(yPredVal-testOutputs)/testOutputs)/len(testOutputs)
        print("YPerf:",yPerf)
        from scipy.stats import pearsonr
        corr, _ = pearsonr(testOutputs[:,0], yPredVal[:,0])
        c2=corr**2
        print('Pearsons correlation: %.9f' % c2)
        
        perfData.append(yPerf)
        if(len(perfData)>1):
            d=np.array(perfData)
            nSamples=(40*np.sqrt(len(d)*np.sum(d**2)-np.sum(d)**2)/np.sum(d))**2
        
        resultsFile.write(str(modelCounter)+"\t"+str(activations)+"\t"+str(nodes)+"\t"+str(ep)+"\t"+str(yPerf)+"\t"+str(c2)+"\t"+str(nSamples)+"\n")
        resultsFile.flush()
        
        model.save('J:/Models/cnc_twin'+str(modelCounter)+'.net')
        
        cursor=cursor+1
        modelCounter=modelCounter+1
        
        
        
        print("Sample size:",nSamples, cursor)
        t.sleep(5)
        if mape>yPerf:
            mape=yPerf
        tf.keras.backend.clear_session()
        del model
        del history_df
        del history
        del sess
    gc.collect()
    return mape


# hill climbing local search algorithm
def hillclimbing(objective, numIterations, stepSize, params):
    
    
	# store the initial point
    solution = params
	# evaluate the initial point
    solution_eval = objective(params)
    
	# run the hill climbü
    for i in range(0, numIterations):
        params[2]=int(np.floor(solution[2]+np.random.rand()*stepSize))
		# evaluate candidate point
        candidte_eval = objective(params)
		# check if we should keep the new point
        
        ################################################
        #minimzation
        if candidte_eval <= solution_eval:  
        ################################################
            
            
			# store the new point
            solution, solution_eval = params, candidte_eval
    return [solution, solution_eval]

# iterated local search algorithm
def iterated_local_search(objective, numRestarts, stepSize, numIterations):
	# define starting point
    
    actFunctions=[['elu','relu','relu','elu','softplus'],
                  ['softplus','softplus','softplus','softplus','softplus'],
                  ['relu','relu','relu','relu','softplus']]
    
    hidden = [10,10,10,10,1]
    best_eval = objective([actFunctions[0], hidden, 200])
    print("nres ", numRestarts)
    for n in range(0, numRestarts):
        for i in range(0, len(actFunctions)):
            if i==0:
                nodes=[]
                ix=int(np.ceil(np.random.rand()*(len(actFunctions[i])-1))-1)
                for j in range(0, len(actFunctions[i])-1):
                    if j==ix:
                        hidden[j]=int(np.floor(hidden[j]+np.random.rand()*stepSize))
                    nodes.append(hidden[j])
                nodes.append(1)
            print(nodes)
            solution, solution_eval = hillclimbing(objective, numIterations, 5000, [actFunctions[i],
                                                                                                nodes, 200])
    		# check for new best
            if solution_eval < best_eval:
                best, best_eval = solution, solution_eval
                print('Restart %d, best: f(%s) = %.5f' % (n, best, best_eval))
    
    return [best, best_eval]
 
# define the total iterations
n_iter = 5
# define the maximum step size
s_size = 20
# total number of random restarts
n_restarts = 100
# perturbation step size
p_size = 1.0
# perform the hill climbing search

start = t.time()

best, score = iterated_local_search(objective, n_restarts, s_size, n_iter)
end = t.time()
print('Done!')
print('f(%s) = %f' % (best, score))
print("Time:", end-start)

resultsFile.close()




#model = ke.models.load_model('Models/cnc_twin17.net')
#model.save('Models/cnc_twin9.net')

#minArr=np.array([125,10,0.01]).reshape(1,3)
#maxArr=np.array([1000,200,1.6]).reshape(1,3)
#rangeArr=maxArr-minArr
#motion=100
#speed=500
#rat=motion/speed
#inputs=np.array([speed, motion, rat]).reshape(1,3)
#normInputs=(inputs-minArr)/rangeArr
#ypred = np.array(model.predict(normInputs).flatten()).reshape(1,1)
#yPredVal=ypred*rangeVal[0,0]+minVal[0,0]
#print(yPredVal)

