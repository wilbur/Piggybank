/*
 * Copyright 2010 LinkedIn, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
 
/* 
 * By Russell Jurney
 */

package wilbur.can.hack.stats;

import com.linkedin.pig.SimpleEvalFunc;

/*

    set job.name 'Bucketize Even Example'

    REGISTER piggybank.jar;

    DEFINE BucketizeEven wilbur.can.hack.stats.BucketizeEven();
    A = LOAD 'title_counts' AS (title:chararray, total:int);
    B = FOREACH A GENERATE title, (float)(total/1.0) AS total:float;
    C = GROUP B ALL;
    D = FOREACH C GENERATE MAX(B.total) as highRange, MIN(B.total) AS lowRange, 1 as stub;
    E = FOREACH B GENERATE title AS title, total AS total, 1 as stub;
    F = JOIN D BY stub, E by stub;
    G = FOREACH F GENERATE D::highRange AS highRange, D::lowRange AS lowRange, E::title AS title, E::total AS total;
    H = FOREACH G GENERATE title, BucketizeEven(lowRange, highRange, 10, total) AS label;
    I = GROUP H BY label;
    J = FOREACH I GENERATE group, SIZE($1) AS total;
    K = ORDER J BY total;
    DUMP K;

 */
public class BucketizeEven extends SimpleEvalFunc<String>
{

    public BucketizeEven() { }

    public String call(Float lowRange, Float highRange, Integer buckets, Float bucketeer)
    {
        // Get the range, the number of steps and the step size
        Float range = highRange - lowRange;
        Float stepSize = range/buckets;
        
        String name = new String();
        for(Float i = lowRange; i < highRange; i += stepSize)
        {
            Float nextRange = i + stepSize;
            if(bucketeer > i && bucketeer < nextRange)
            {
                name = bucketName(i, nextRange);
                break;
            }
        }
        return name;
    }
    
    String bucketName(Float lowRange, Float highRange) {
        return String.format("%s-%s",lowRange, highRange);
    }
}
