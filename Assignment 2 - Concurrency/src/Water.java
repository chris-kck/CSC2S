/**
 * <h1><b>ParallelBasinClassification</b></h1>
 * Water Class to Store Terrain Water Details Object.
 * <br><hr>
 *
 * @author  Chris Kateera
 * @version 1.0
 * @since   20-09-2020
 */
public class Water {

    float wDepth;
    float wSurface;
    float tHeight;

    /**
     * Class constructor instantiating an object
     * @param wDepth
     * @param tHeight
     */
    public Water (float wDepth, float tHeight){
        this.wDepth=wDepth; //initial depth
        this.tHeight=tHeight; //initial Height
        this.wSurface= wDepth + tHeight; //initial wSurface level
        //wS = wD + tH
    }

    /**
     *Function to change depth of a point.
     * @param drop either a water addition or subtraction
     */
    public void changeDepth (float drop){
        this.wDepth+=drop;
        this.wSurface= this.wDepth + this.tHeight; //increasing depth also increases surface
    }

    /**
     * Function to remove water completely from a given point.
     */
    public void removeDepth (){
        this.wDepth=0;
        this.wSurface = this.tHeight;
    }
}
