package com.itcag.dl.train;

import java.util.Properties;

/**
 * This class inserts configuration parameters for a neural network.
 * The purpose of this class is provide not only the configuration,
 * but also to document how various parameters affect the training,
 * and which options are available for every parameter.
 */
public class MultiLayerNetworkConfiguration {

    /**
     * This method inserts configuration parameters specifically for the MultiLayerNetwork class.
     * @param config Instance of the Java Property class.
     */
    public final void get(Properties config) {
        
        
        
    }
    
    /**
     * This parameter selects the updater, and sets the learning rate for it.
     * Stochastic Gradient Descent, the most common learning algorithm in deep learning,
     * relies on the weights in hidden layers and the learning rate.
     * The updaters determine the learning rate,
     * and how the neural network converges on its most performant state.
     * The available options are provided by the {@link com.itcag.dl.train.Updaters Updaters} enum.
     * @param config Instance of the Java Property class.
     */
    public final void insertMultiLayerNetworkUpdater(Properties config) {
        config.setProperty("updater", Updaters.RMS_PROP_UPDATER.name());
        config.setProperty("learningRate", "0.0018");
    }

    /**
     * Regularization is a strategy that minimizes the risks of overfitting.
     * It is a form of regression, that "regularizes" (constrains) the coefficient estimates
     * by adding a penalty value. 
     * There are two standard regularization strategies:
     * L1 norm (also known as "Lasso") - besides restricting coefficients also eliminates low coefficient features, and is useful in feature selection.
     * L2 norm (also known as "Ridge") - the higher the coefficients, the more they get penalized.
     * @param config Instance of the Java Property class.
     */
    public final void insertMultiLayerNetworkRegularization(Properties config) {
//        config.setProperty("l1", "1e-5");
        config.setProperty("l2", "1e-5");
    }
    
    /**
     * This parameters determines which initial values will be set.
     * The available options are provided by the {@link com.itcag.dl.train.InitialWeightStrategies InitialWeightStrategies} enum.
     * @param config Instance of the Java Property class.
     */
    public final void insertMultiLayerNetworkWeightInit(Properties config) {
        config.setProperty("weightInit", InitialWeightStrategies.XAVIER.name());
    }
    
}
