import { HTMLInputTypeAttribute } from "react";

export interface InputProps {
  className?: string;
  type?: HTMLInputTypeAttribute;
  autoFocus?: boolean;
  placeholder?: string;
  value: string | number;
  onChange: React.ChangeEventHandler<HTMLInputElement> | undefined;
  required?: boolean;
  onKeyDown?: React.KeyboardEventHandler<HTMLInputElement> | undefined;
}
