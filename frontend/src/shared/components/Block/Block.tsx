import { ReactNode } from "react";
import styles from "./Block.module.css";

export const Block = ({
  children,
  className,
}: {
  children: ReactNode;
  className?: string;
}) => {
  return <div className={`${styles["block"]} ${className}`}>{children}</div>;
};
