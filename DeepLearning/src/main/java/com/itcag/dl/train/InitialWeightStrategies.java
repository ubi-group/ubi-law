package com.itcag.dl.train;

/**
 * <p>Lists all available strategies to set the initial network values.</p>
 * 
 */
public enum InitialWeightStrategies {

    /**
     * Sample weights from a provided distribution.
     */
     DISTRIBUTION,
     
     /**
      * Generate weights as zeros.
      */
     ZERO,
     
     /**
      * All weights are set to 1.
      */
     ONES,

    /**
     * A version of XAVIER_UNIFORM for sigmoid activation functions. U(-r,r) with r=4*sqrt(6/(fanIn + fanOut)).
     */
     SIGMOID_UNIFORM,
    
     /**
      * Normal/Gaussian distribution, with mean 0 and standard deviation 1/sqrt(fanIn). This is the initialization recommented in Klambauer et al. 2017, "Self-Normalizing Neural Network". Equivalent to DL4J's XAVIER_FAN_IN and LECUN_NORMAL (i.e. Keras' "lecun_normal").
      */
    NORMAL,
    
    /**
     * Uniform U[-a,a] with a=3/sqrt(fanIn).
     */
    LECUN_UNIFORM,
    
    /**
     * Uniform U[-a,a] with a=1/sqrt(fanIn). "Commonly used heuristic" as per Glorot and Bengio 2010.
     */
    UNIFORM,

    /**
     * As per Glorot and Bengio 2010: Gaussian distribution with mean 0, variance 2.0/(fanIn + fanOut).
     */
    XAVIER,
    
    /**
     * As per Glorot and Bengio 2010: Uniform distribution U(-s,s) with s = sqrt(6/(fanIn + fanOut)).
     */
    XAVIER_UNIFORM,
    
    /**
     * Similar to Xavier, but 1/fanIn -> Caffe originally used this.
     */
    XAVIER_FAN_IN,
    
    /**
     * Xavier weight init in DL4J up to 0.6.0. XAVIER should be preferred.
     */
    XAVIER_LEGACY,
    
    /**
     * He et al. (2015), "Delving Deep into Rectifiers". Normal distribution with variance 2.0/nIn.
     */
    RELU,

    /**
     * He et al. (2015), "Delving Deep into Rectifiers". Uniform distribution U(-s,s) with s = sqrt(6/fanIn).
     */
    RELU_UNIFORM,
    
    /**
     * Weights are set to an identity matrix. Note: can only be used with square weight matrices.
     */
    IDENTITY,
    
    /**
     * Gaussian distribution with mean 0, variance 1.0/(fanIn).
     */
    VAR_SCALING_NORMAL_FAN_IN,
    
    /**
     * Gaussian distribution with mean 0, variance 1.0/(fanOut).
     */
    VAR_SCALING_NORMAL_FAN_OUT,
    
    /**
     * Gaussian distribution with mean 0, variance 1.0/((fanIn + fanOut)/2).
     */
    VAR_SCALING_NORMAL_FAN_AVG,

    /**
     * Uniform U[-a,a] with a=3.0/(fanIn).
     */
    AR_SCALING_UNIFORM_FAN_IN,
    
    /**
     * Uniform U[-a,a] with a=3.0/(fanOut).
     */
    VAR_SCALING_UNIFORM_FAN_OUT,
    
    /**
     * Uniform U[-a,a] with a=3.0/((fanIn + fanOut)/2).
     */
    VAR_SCALING_UNIFORM_FAN_AVG,

}
