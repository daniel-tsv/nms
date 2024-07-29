import { ReactNode } from "react";
import styles from "./Layout.module.css";
export const Layout = ({
  children,
  className,
}: {
  children: ReactNode;
  className?: string;
}) => {
  return <div className={`${styles["layout"]} ${className}`}>{children}</div>;
};
