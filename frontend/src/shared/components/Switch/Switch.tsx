import styles from "./Switch.module.css";

export const Switch = ({
  onToggle,
  value,
  className,
}: {
  onToggle: () => void;
  value: boolean;
  className?: string;
}) => {
  return (
    <div className={styles["switch"]}>
      <input type="checkbox" onChange={onToggle} checked={value} />

      <span
        onClick={onToggle}
        className={`${styles["slider"]} ${styles["round"]} ${className}`}
      ></span>
    </div>
  );
};
