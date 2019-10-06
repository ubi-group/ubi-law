package com.itcag.legalyzer.train;

/**
 * <p>Lists all available updaters.</p>
 * <p>For a more detailed explanation @see <a href="http://www.cs.toronto.edu/~tijmen/csc321/slides/lecture_slides_lec6.pdf">Overview of mini-batch gradient descent</a>.</p>
 * @see <a href="http://www.cs.toronto.edu/~tijmen/csc321/slides/lecture_slides_lec6.pdf">Overview of mini-batch gradient descent</a>
 */
public enum Updaters {

    NADAM_UPDATER,
    
    NESTEROVS_UPDATER,
    
    /**
     * Divide the learning rate for a weight
     * by a running average of the magnitudes
     * of recent gradients for that weight.
     */
    RMS_PROP_UPDATER,
    
    ADA_GRAD_UPDATER,
    
    ADA_MAX_UPDATER,
    
    NO_OP_UPDATER,
    
    ADAM_UPDATER,
    
    ADA_DELTA_UPDATER,
    
    SGD_UPDATER,
    
    GRADIENT_UPDATER,
    
    AMS_GRAD_UPDATER,
    
}
