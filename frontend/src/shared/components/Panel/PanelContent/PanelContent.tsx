import { ReactNode } from "react";
import styles from "./PanelContent.module.css";

export const PanelContent = ({
  closed,
  children,
}: {
  closed: boolean;
  children: ReactNode;
}) => {
  return <div className={styles["panel-content"]}>{closed || children}</div>;
};
