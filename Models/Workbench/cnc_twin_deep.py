# -*- coding: utf-8 -*-
"""
Created on Thu Oct  6 17:36:35 2022

@author: Muhammet Rasit Cesur, Elif Cesur
"""

# -*- coding: utf-8 -*-
"""
Spyder Editor

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
import sklearn.preprocessing as pp
from pickle import dump
from pickle import load
import sys


class WorkbenchModel:
    def __init__(self, description, logFilePath, modelFolderPath, trainingPlotPath):
        
        # To Randomize.
        np.random.seed(1)
        tf.compat.v1.disable_eager_execution()
        tf.set_random_seed(1)
        
        self.description = description
        self.modelFolderPath=modelFolderPath
        self.trainingPlotPath=trainingPlotPath
        self.logFilePath=logFilePath
        self.normalizer = pp.MaxAbsScaler()
    
    def setDataset(self, trainingDataset, testDataset):
        self.trainingDataset = trainingDataset
        self.testDataset=testDataset

    def setInputNormalizers(self, normalizers):
        self.inputNormalizers=normalizers    
        
    def setOutputNormalizers(self, normalizers):
        self.outputNormalizers=normalizers    
        
    def saveInputNormalizers(self, fileName):
        dump(self.inputNormalizers, open(fileName, 'wb'))
         
    def loadInputNormalizers(self, fileName):
        self.inputNormalizers=load(open(fileName, 'rb'))
        
    def saveOutputNormalizers(self, fileName):
        dump(self.outputNormalizers, open(fileName, 'wb'))
             
    def loadOutputNormalizers(self, fileName):
        self.outputNormalizers=load(open(fileName, 'rb'))
        
    def normalizeDataset(self, inputColumns, outputColumns):
    
        # A merged datasets is created with training and test datasets for normalization
        columns=[]
        for i in inputColumns:
            columns.append(i)
        
        for i in outputColumns:
            columns.append(i)
        
        print()
        
        dataset=np.concatenate((self.trainingDataset[:,columns], 
                                self.testDataset[:,columns]), axis=0)

        
        lenTraining=self.trainingDataset.shape[0]
        lenDataset=dataset.shape[0]
        
        lenOutputs=len(outputColumns)
        lenInputs=len(inputColumns)
        lenColumns=len(columns)
        outputDataset=dataset[:,range(0,lenOutputs)]
        inputDataset=dataset[:,range(lenOutputs,lenColumns)]        
        
        for i in range(0,lenOutputs):
            self.outputNormalizers[i].fit(outputDataset[:,i].reshape(-1, 1))
        for i in range(0,lenInputs):
            self.inputNormalizers[i].fit(inputDataset[:,i].reshape(-1, 1))
            
        
        normalizedOutputs=self.outputNormalizers[0].transform(outputDataset[:,0].reshape(-1, 1))
        for i in range(1,lenOutputs):
            normalizedOutputs=np.append(normalizedOutputs, self.outputNormalizers[i].transform(outputDataset[:,i].reshape(-1, 1)), axis=1)
            
        normalizedInputs=self.inputNormalizers[0].transform(inputDataset[:,0].reshape(-1, 1))
        for i in range(1,lenInputs):
            normalizedInputs=np.append(normalizedInputs, self.inputNormalizers[i].transform(inputDataset[:,i].reshape(-1, 1)), axis=1)
        
        self.trainY=normalizedOutputs[range(0,lenTraining), :]
        self.testY=normalizedOutputs[range(lenTraining,lenDataset), :]        
        self.testOutputs=outputDataset[range(lenTraining,lenDataset), :]
        
        self.trainX=normalizedInputs[range(0,lenTraining), :]
        self.testX=normalizedInputs[range(lenTraining,lenDataset), :]
        
        return [self.trainX, self.trainY, self.testX, self.testY]

    def buildProcessingTimeModel(self, motionColumn, feedRateColumn, timeColumn):
        
        self.setInputNormalizers([pp.MaxAbsScaler(),pp.MaxAbsScaler(),pp.MaxAbsScaler()])
        self.setOutputNormalizers([pp.MaxAbsScaler()])
        
        # Appending a special column depending on physical model to dataset
        self.trainingDataset=np.append(self.trainingDataset, (self.trainingDataset[:,motionColumn] / 
                                        self.trainingDataset[:,feedRateColumn]).reshape(self.trainingDataset.shape[0],1), axis=1)
        
        self.testDataset=np.append(self.testDataset, (self.testDataset[:,motionColumn] / 
                                        self.testDataset[:,feedRateColumn]).reshape(self.testDataset.shape[0],1), axis=1)
        
        
        self.normalizeDataset([feedRateColumn, motionColumn, self.trainingDataset.shape[1]-1],[timeColumn])
        
        
        
        actFunctions=[['softplus','softplus','softplus','softplus','softplus'],
                      ['relu','relu','relu','relu','relu'],
                      ['relu','relu','relu','relu','softplus'],
                      ['elu','relu','relu','elu','softplus']]
        
        hidden = [10,10,10,10,1]
        hiddenStepSize=20
        startEpocs=200
        epocStepSize=5000
        numIterations=5
        numRestarts=100
        
        
        self.logFile = open(self.logFilePath, "a+")
        
        start = t.time()
        
        best, score = self.iterated_local_search_topology_optimization(actFunctions, hidden, hiddenStepSize, startEpocs, 
                                                        epocStepSize, numIterations, numRestarts)
        
        end = t.time()
        print('Done!')
        print('f(%s) = %f' % (best, score))
        print("Time:", end-start)

        self.logFile.close()
        
        return [best, score]

    def buildModel(self, params):
        
        # assigning the maximum value at beginning for searching the minimum value
        mape=sys.float_info.max
        
        # model parameters
        activations=params[0]
        nodes=params[1]
        ep=params[2]
        
        self.modelCounter=self.modelCounter+10
        cursor=0
        perfData=[]
        nSamples=5
        
        while cursor<=nSamples and cursor<nSamples:
            
            sess = tf.Session()        
            K.set_session(sess)
            model = ke.Sequential()
            model.add(L.Dense(nodes[0], input_dim=3, activation=activations[0]))
            for i in range(1, len(nodes)):
                model.add(L.Dense(nodes[i], activation=activations[i]))
           
    
            model.compile(loss='mse', optimizer='adam', metrics=['mse', 'mae'])
            
            history = model.fit(self.trainX, self.trainY, epochs=ep, 
                                validation_data=(self.testX, self.testY))
            
            history_df = p.DataFrame(history.history)
            plt.figure(self.plotCounter)
            plt.plot(history_df['loss'], label='loss')
            plt.plot(history_df['val_loss'], label='test loss')
            #plt.legend()
            plt.savefig(self.trainingPlotPath+str(self.modelCounter)+'_loss.png')
            plt.close() 
            self.plotCounter=self.plotCounter+1
            
            ypred = np.array(model.predict(self.testX).flatten()).reshape(self.testOutputs.shape)
            
            yPredVal=self.outputNormalizers[0].inverse_transform(ypred[:,0].reshape(-1, 1))
            for i in range(1,testOutputs.shape[1]):
                yPredVal=np.append(yPredVal, self.outputNormalizers[i].inverse_transform(ypred[:,i].reshape(-1, 1)), axis=1)
            
            
            yPerf=sum(abs(yPredVal-self.testOutputs)/self.testOutputs)/testOutputs.shape[0]
            print("YPerf:",yPerf)
            from scipy.stats import pearsonr
            corr, _ = pearsonr(testOutputs[:,0], yPredVal[:,0])
            c2=corr**2
            print('Pearsons correlation: %.9f' % c2)
            
            # Calculating statistically meaningful number of samples.
            # If number of meaningful samples is smaller than user 
            # defined threshold, there is no need more trials.
            perfData.append(yPerf)
            if(len(perfData)>1):
                d=np.array(perfData)
                numSamples=(40*np.sqrt(len(d)*np.sum(d**2)-np.sum(d)**2)/np.sum(d))**2
                if(numSamples<nSamples): 
                    nSamples=np.ceil(numSamples)
            
            self.logFile.write(str(self.modelCounter)+"\t"+str(activations)+"\t"+str(nodes)+"\t"+str(ep)+"\t"+str(yPerf)+"\t"+str(c2)+"\t"+str(nSamples)+"\n")
            self.logFile.flush()
            
            model.save(self.modelFolderPath+str(self.modelCounter)+'.net')
            
            cursor=cursor+1
            self.modelCounter=self.modelCounter+1
            
            
            
            print("Sample size:",nSamples, cursor)
            t.sleep(5)
            if mape>yPerf:
                mape=yPerf
                
            # Cleaninig memory and gpu memory.
            tf.keras.backend.clear_session()
            del model
            del history_df
            del history
            del sess
        # Cleaning memory
        gc.collect()
        return mape
    
    
    # hill climbing local search algorithm
    def hillclimbing(self, numIterations, stepSize, params):
        
        
    	# store the initial point
        solution = params
    	# evaluate the initial point
        solution_eval = self.buildModel(params)
        
    	# run the hill climb
        for i in range(0, numIterations):
            params[2]=int(np.floor(solution[2]+np.random.rand()*stepSize))
    		# evaluate candidate point
            candidte_eval = self.buildModel(params)
    		# check if we should keep the new point
            
            ################################################
            #minimzation
            if candidte_eval <= solution_eval:  
            ################################################
                
                
    			# store the new point
                solution, solution_eval = params, candidte_eval
        return [solution, solution_eval]
    

    def iterated_local_search_topology_optimization(self, actFunctions, hidden, hiddenStepSize, startEpocs, epocStepSize, 
                                                    numIterations, numRestarts):
    	# 
        self.modelCounter=0
        self.plotCounter=0
       
        best_eval = self.buildModel([actFunctions[0], hidden, startEpocs])
        print("nres ", numRestarts)
        for n in range(0, numRestarts):
            for i in range(0, len(actFunctions)):
                if i==0:
                    nodes=[]
                    ix=int(np.ceil(np.random.rand()*(len(actFunctions[i])-1))-1)
                    for j in range(0, len(actFunctions[i])-1):
                        if j==ix:
                            hidden[j]=int(np.floor(hidden[j]+np.random.rand()*hiddenStepSize))
                        nodes.append(hidden[j])
                    nodes.append(1)
                print(nodes)
                solution, solution_eval = self.hillclimbing(numIterations, epocStepSize, [actFunctions[i], nodes, startEpocs])
        		# check for new best
                if solution_eval < best_eval:
                    best, best_eval = solution, solution_eval
                    print('Restart %d, best: f(%s) = %.5f' % (n, best, best_eval))
        
        return [best, best_eval]
 

df=p.read_table("DataSets/cnc_twin2.txt", sep="\t", encoding="utf-8")

data=df.to_numpy()
data=np.append(data, (data[:,2]/data[:,1]).reshape(len(data),1), axis=1)
testOutputs=data[np.in1d(df['Motion'],[15, 35, 65, 85, 125, 165, 185, 225, 285]),0].reshape(-1,1)
minVal=np.repeat([[min(data[:,0]),min(data[:,1]),min(data[:,2]),min(data[:,3])]], len(data), axis=0).reshape(len(data),4)
maxVal=np.repeat([[max(data[:,0]),max(data[:,1]),max(data[:,2]),max(data[:,3])]], len(data), axis=0).reshape(len(data),4)

rangeVal=maxVal-minVal
normVal=(data-minVal)/rangeVal
testVal=data[np.in1d(df['Motion'],[15, 35, 65, 85, 125, 165, 185, 225, 285])]
trainingVal=data[np.in1d(df['Motion'],[10, 25, 50, 75, 100, 115, 135, 150, 175, 200, 215, 235, 250, 300])]

model=WorkbenchModel('Test','result.txt','','')

model.setDataset(trainingVal, testVal)

model.buildProcessingTimeModel(1,2,0)








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

