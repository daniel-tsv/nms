import styles from "./Button.module.css";

interface ButtonProps {
  label?: string;
  onClick?: () => void;
  iconPath?: string;
  type?: "button" | "submit" | "reset";
  variant?: "primary" | "secondary" | "accent" | "danger";
  size?: "small" | "medium" | "large" | "max-size";
  className?: string;
  children?: React.ReactNode;
}

export const Button = ({
  label,
  onClick,
  iconPath,
  type,
  variant = "primary",
  size = "max-size",
  className,
  children,
}: ButtonProps) => {
  const classNames = `
    ${styles.button}
    ${styles[`button--${variant}`]}
    ${styles[`button--${size}`]}
    ${className ?? ""}
  `;

  return (
    <button type={type ?? "button"} className={classNames} onClick={onClick}>
      {iconPath && (
        <img alt="" src={iconPath} className={styles["button-icon"]} />
      )}
      {label}
      {children}
    </button>
  );
};
