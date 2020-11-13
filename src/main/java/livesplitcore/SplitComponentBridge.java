package livesplitcore;

public class SplitComponentBridge {
    public static long getColumnsLength(SplitsComponentStateRef ref, long index) {
        SplitComponentState state = new SplitComponentState(ref.ptr);
        return state.columnsLen(index);
    }
}
