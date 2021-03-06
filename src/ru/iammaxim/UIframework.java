package ru.iammaxim;

public class UIframework {
    public static boolean isOutAllowed = true;

    public static boolean checkMirror(Mirror mirror, double x, double y) {
        if (x > Math.min(Math.min(mirror.lt.x, mirror.rt.x), Math.min(mirror.lb.x, mirror.rb.x)) && x < Math.max(Math.max(mirror.lt.x, mirror.rt.x), Math.max(mirror.lb.x, mirror.rb.x)))
            if (-y > Math.min(Math.min(mirror.lt.y, mirror.rt.y), Math.min(mirror.lb.y, mirror.rb.y)) && -y < Math.max(Math.max(mirror.lt.y, mirror.rt.y), Math.max(mirror.lb.y, mirror.rb.y))) {
                for (Mirror mirror1 : Physics.mirrors) mirror1.setSelected(false);
                return true;
            }
        return false;
    }

    public static double getNormalizedMousePosX(double x) {
        return (x - DrawUtils.width / 2) * 2;
    }

    public static double getNormalizedMousePosY(double y) {
        return (y - DrawUtils.height / 2) * 2;
    }

}
