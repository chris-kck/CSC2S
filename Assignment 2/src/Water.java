public class Water {

    float wDepth;
    float wSurface;
    float tHeight;
    public Water (float wDepth, float tHeight){
        this.wDepth=wDepth; //initial depth
        this.tHeight=tHeight; //initial Height
        this.wSurface= wDepth + tHeight; //initial wSurface level
        //wS = wD + tH
    }

    public void changeDepth (float drop){
        this.wDepth+=drop;
        this.wSurface= this.wDepth + this.tHeight; //increasing depth also increases surface
    }


    public void removeDepth (){
        this.wDepth=0;
        this.wSurface = this.tHeight;
    }
}
