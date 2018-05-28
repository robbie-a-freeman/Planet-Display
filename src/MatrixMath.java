import org.joml.*;

public class MatrixMath {

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale) {
        Matrix4f matrix = new Matrix4f(); // identity matrix
        matrix.translate(new Vector3f(translation.x, translation.y, translation.z));
        //System.out.println("After translation: \n" + matrix);
        matrix.rotateAffineXYZ(rotation.x, rotation.y, rotation.z);
        //System.out.println("After rotation: \n" + matrix);
        matrix.scale(new Vector3f(scale.x, scale.y, scale.z));
        //System.out.println("After scaling: \n" + matrix);

        return matrix;
    }

}
