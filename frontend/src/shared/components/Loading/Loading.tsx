import { useEffect, useMemo, useRef, useState } from "react";
import { Block } from "../Block/Block";
import styles from "./Loading.module.css";

export const Loading = ({ className }: { className?: string }) => {
  const loadingTextStates: string[] = useMemo(
    () => ["Loading.", "Loading..", "Loading..."],
    []
  );

  const [loadingText, setLoadingText] = useState(loadingTextStates[0]);
  const currentIndexRef = useRef(0);

  useEffect(() => {
    currentIndexRef.current =
      (currentIndexRef.current + 1) % loadingTextStates.length;
    setTimeout(
      () => setLoadingText(loadingTextStates[currentIndexRef.current]),
      1000
    );
  }, [loadingTextStates]);

  return (
    <Block className={`${styles["loading"]} ${className}`}>
      <img className={styles["loading-icon"]} alt="" src="/loading.svg" />
      {loadingText}
    </Block>
  );
};
