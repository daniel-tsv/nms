import styles from "./Input.module.css";
import { InputProps } from "./InputProps";

export const Input = ({
  className,
  type,
  autoFocus,
  placeholder,
  value,
  onChange,
  required,
  onKeyDown,
}: InputProps) => {
  return (
    <input
      className={`${styles["input"]} ${className}`}
      autoFocus={autoFocus ?? false}
      type={type ?? "text"}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
      required={required}
      onKeyDown={onKeyDown}
    />
  );
};
