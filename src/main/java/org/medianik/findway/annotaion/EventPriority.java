package org.medianik.findway.annotaion;

/**
 * <p>All events can be grouped by their priority:</p>
 * <p>if we have groupPre[], groupMid[], groupPost[],</p>
 * <p>then they have to be called in such sequence:</p>
 * <p>groupPre[i] < groupMid[j] < groupPost[k] for any i, j, k</p>
 */
public enum EventPriority{
    PRE_PROCESSING,
    PRE_MID_PROCESSING,
    MID_PROCESSING,
    POST_MID_PROCESSING,
    POST_PROCESSING;
}
